package com.xiawei.testruoyi.learn.server.application.service.impl;

import com.xiawei.testruoyi.learn.base.exceptions.BusinessException;
import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.application.bo.user.UserBO;
import com.xiawei.testruoyi.learn.server.application.bo.user.UserCreateBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.UserDO;
import com.xiawei.testruoyi.learn.server.domain.user.service.UserDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.UserConvertor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 用户应用服务单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserAppService 单元测试")
class UserAppServiceImplTest {

	@Mock
	private UserDomainService userDomainService;

	@Mock
	private UserConvertor userConvertor;

	@InjectMocks
	private UserAppServiceImpl userAppService;

	private UserCreateBO createBO;
	private UserDO userDO;
	private UserBO userBO;

	@BeforeEach
	void setUp() {
		createBO = new UserCreateBO();
		createBO.setUsername("testuser");
		createBO.setPassword("password123");
		createBO.setNickname("测试用户");
		createBO.setEmail("test@example.com");

		userDO = new UserDO();
		userDO.setId(1L);
		userDO.setUsername("testuser");
		userDO.setStatus(1);

		userBO = new UserBO();
		userBO.setId(1L);
		userBO.setUsername("testuser");
	}

	// ========== 创建用户 ==========

	@Nested
	@DisplayName("创建用户测试")
	class CreateUserTests {

		@Test
		@DisplayName("参数为null时抛出ParamException")
		void createUser_nullParam_throwsParamException() {
			assertThatThrownBy(() -> userAppService.createUser(null))
					.isInstanceOf(ParamException.class)
					.hasMessage("创建参数不能为空");
		}

		@Test
		@DisplayName("用户名为空时抛出ParamException")
		void createUser_emptyUsername_throwsParamException() {
			createBO.setUsername("");
			assertThatThrownBy(() -> userAppService.createUser(createBO))
					.isInstanceOf(ParamException.class)
					.hasMessage("用户名不能为空");
		}

		@Test
		@DisplayName("用户名过短时抛出ParamException")
		void createUser_usernameTooShort_throwsParamException() {
			createBO.setUsername("a");
			assertThatThrownBy(() -> userAppService.createUser(createBO))
					.isInstanceOf(ParamException.class)
					.hasMessage("用户名长度必须在2-20个字符之间");
		}

		@Test
		@DisplayName("用户名过长时抛出ParamException")
		void createUser_usernameTooLong_throwsParamException() {
			createBO.setUsername("a".repeat(21));
			assertThatThrownBy(() -> userAppService.createUser(createBO))
					.isInstanceOf(ParamException.class)
					.hasMessage("用户名长度必须在2-20个字符之间");
		}

		@Test
		@DisplayName("密码为空时抛出ParamException")
		void createUser_emptyPassword_throwsParamException() {
			createBO.setPassword("");
			assertThatThrownBy(() -> userAppService.createUser(createBO))
					.isInstanceOf(ParamException.class)
					.hasMessage("密码不能为空");
		}

		@Test
		@DisplayName("正常创建用户返回用户ID")
		void createUser_validInput_returnsUserId() {
			when(userConvertor.toDomainFromCreateBO(createBO)).thenReturn(userDO);
			doAnswer(invocation -> {
				UserDO user = invocation.getArgument(0);
				user.setId(1L);
				return null;
			}).when(userDomainService).save(any(UserDO.class));

			Long id = userAppService.createUser(createBO);
			assertThat(id).isEqualTo(1L);
			verify(userDomainService).validateUsernameNotExists("testuser");
		}
	}

	// ========== 删除用户 ==========

	@Nested
	@DisplayName("删除用户测试")
	class DeleteUserTests {

		@Test
		@DisplayName("ID为null时抛出ParamException")
		void deleteUser_nullId_throwsParamException() {
			assertThatThrownBy(() -> userAppService.deleteUser(null))
					.isInstanceOf(ParamException.class)
					.hasMessage("用户ID不合法");
		}

		@Test
		@DisplayName("ID为0时抛出ParamException")
		void deleteUser_zeroId_throwsParamException() {
			assertThatThrownBy(() -> userAppService.deleteUser(0L))
					.isInstanceOf(ParamException.class)
					.hasMessage("用户ID不合法");
		}

		@Test
		@DisplayName("ID为负数时抛出ParamException")
		void deleteUser_negativeId_throwsParamException() {
			assertThatThrownBy(() -> userAppService.deleteUser(-1L))
					.isInstanceOf(ParamException.class)
					.hasMessage("用户ID不合法");
		}
	}

	// ========== 重置密码 ==========

	@Nested
	@DisplayName("重置密码测试")
	class ResetPasswordTests {

		@Test
		@DisplayName("ID为null时抛出ParamException")
		void resetPassword_nullId_throwsParamException() {
			assertThatThrownBy(() -> userAppService.resetPassword(null, "newpass"))
					.isInstanceOf(ParamException.class)
					.hasMessage("用户ID不合法");
		}

		@Test
		@DisplayName("新密码为空时抛出ParamException")
		void resetPassword_emptyPassword_throwsParamException() {
			assertThatThrownBy(() -> userAppService.resetPassword(1L, ""))
					.isInstanceOf(ParamException.class)
					.hasMessage("新密码不能为空");
		}

		@Test
		@DisplayName("新密码为null时抛出ParamException")
		void resetPassword_nullPassword_throwsParamException() {
			assertThatThrownBy(() -> userAppService.resetPassword(1L, null))
					.isInstanceOf(ParamException.class)
					.hasMessage("新密码不能为空");
		}
	}

	// ========== 修改状态 ==========

	@Nested
	@DisplayName("修改状态测试")
	class UpdateStatusTests {

		@Test
		@DisplayName("ID为null时抛出ParamException")
		void updateStatus_nullId_throwsParamException() {
			assertThatThrownBy(() -> userAppService.updateStatus(null, 1))
					.isInstanceOf(ParamException.class)
					.hasMessage("用户ID不合法");
		}

		@Test
		@DisplayName("状态为null时抛出ParamException")
		void updateStatus_nullStatus_throwsParamException() {
			assertThatThrownBy(() -> userAppService.updateStatus(1L, null))
					.isInstanceOf(ParamException.class)
					.hasMessage("状态值不合法");
		}

		@Test
		@DisplayName("状态值非法时抛出ParamException")
		void updateStatus_invalidStatus_throwsParamException() {
			assertThatThrownBy(() -> userAppService.updateStatus(1L, 2))
					.isInstanceOf(ParamException.class)
					.hasMessage("状态值不合法");
		}

		@Test
		@DisplayName("状态值0-禁用合法")
		void updateStatus_status0_isValid() {
			userAppService.updateStatus(1L, 0);
			verify(userDomainService).updateStatus(1L, 0);
		}

		@Test
		@DisplayName("状态值1-正常合法")
		void updateStatus_status1_isValid() {
			userAppService.updateStatus(1L, 1);
			verify(userDomainService).updateStatus(1L, 1);
		}
	}

	// ========== 查询用户 ==========

	@Nested
	@DisplayName("查询用户测试")
	class QueryUserTests {

		@Test
		@DisplayName("根据ID查询返回UserBO")
		void getUserById_validId_returnsUserBO() {
			when(userDomainService.getById(1L)).thenReturn(userDO);
			when(userConvertor.toUserBO(userDO)).thenReturn(userBO);

			UserBO result = userAppService.getUserById(1L);
			assertThat(result).isNotNull();
			assertThat(result.getId()).isEqualTo(1L);
			assertThat(result.getUsername()).isEqualTo("testuser");
		}

		@Test
		@DisplayName("分页查询返回PageBO")
		void pageUsers_returnsPageBO() {
			PageDO<UserDO> pageDO = new PageDO<>();
			pageDO.setCurrent(1);
			pageDO.setSize(10);
			pageDO.setTotal(1);
			pageDO.setRecords(List.of(userDO));

			when(userDomainService.findPage(1, 10, null, null)).thenReturn(pageDO);
			when(userConvertor.toUserBOList(anyList())).thenReturn(List.of(userBO));

			PageBO<UserBO> result = userAppService.pageUsers(1, 10, null, null);
			assertThat(result).isNotNull();
			assertThat(result.getTotal()).isEqualTo(1);
			assertThat(result.getRecords()).hasSize(1);
		}
	}
}
