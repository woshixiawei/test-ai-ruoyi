package com.xiawei.testruoyi.learn.server.application.service.impl;

import com.xiawei.testruoyi.learn.base.exceptions.BusinessException;
import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.server.application.bo.auth.LoginBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.RoleDO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.UserDO;
import com.xiawei.testruoyi.learn.server.domain.user.service.MenuDomainService;
import com.xiawei.testruoyi.learn.server.domain.user.service.RoleDomainService;
import com.xiawei.testruoyi.learn.server.domain.user.service.UserDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.MenuConvertor;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.UserConvertor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 认证应用服务单元测试
 *
 * <p>注意：AuthAppServiceImpl 依赖 StpUtil 静态方法（sa-token），
 * 纯单元测试无法 Mock 静态方法，因此登录成功流程和权限/角色查询
 * 需在集成测试中验证。此处仅验证参数校验和状态校验逻辑。</p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthAppService 单元测试")
class AuthAppServiceImplTest {

	@Mock
	private UserDomainService userDomainService;

	@Mock
	private RoleDomainService roleDomainService;

	@Mock
	private MenuDomainService menuDomainService;

	@Mock
	private UserConvertor userConvertor;

	@Mock
	private MenuConvertor menuConvertor;

	@InjectMocks
	private AuthAppServiceImpl authAppService;

	private UserDO normalUser;

	@BeforeEach
	void setUp() {
		normalUser = new UserDO();
		normalUser.setId(2L);
		normalUser.setUsername("testuser");
		normalUser.setPassword("$2a$10$hashedpassword");
		normalUser.setStatus(1);
	}

	@Nested
	@DisplayName("登录测试")
	class LoginTests {

		@Test
		@DisplayName("参数为null时抛出ParamException")
		void login_nullParam_throwsParamException() {
			assertThatThrownBy(() -> authAppService.login(null))
					.isInstanceOf(ParamException.class)
					.hasMessage("登录参数不能为空");
		}

		@Test
		@DisplayName("用户名为空时抛出ParamException")
		void login_emptyUsername_throwsParamException() {
			LoginBO bo = new LoginBO();
			bo.setUsername("");
			bo.setPassword("password");

			assertThatThrownBy(() -> authAppService.login(bo))
					.isInstanceOf(ParamException.class)
					.hasMessage("用户名不能为空");
		}

		@Test
		@DisplayName("密码为空时抛出ParamException")
		void login_emptyPassword_throwsParamException() {
			LoginBO bo = new LoginBO();
			bo.setUsername("admin");
			bo.setPassword("");

			assertThatThrownBy(() -> authAppService.login(bo))
					.isInstanceOf(ParamException.class)
					.hasMessage("密码不能为空");
		}

		@Test
		@DisplayName("用户状态禁用时抛出BusinessException")
		void login_disabledUser_throwsBusinessException() {
			normalUser.setStatus(0);
			LoginBO bo = new LoginBO();
			bo.setUsername("testuser");
			bo.setPassword("password");

			when(userDomainService.findByUsername("testuser")).thenReturn(normalUser);

			assertThatThrownBy(() -> authAppService.login(bo))
					.isInstanceOf(BusinessException.class)
					.hasMessage("账号已停用");
		}

		// 注意：登录成功测试需要Mock StpUtil静态方法（sa-token），
		// 纯单元测试无法覆盖，需在集成测试中验证。
		// 此处仅验证参数校验和状态校验逻辑。
	}

	@Nested
	@DisplayName("获取当前用户权限测试")
	class PermissionTests {

		// 注意：获取权限测试需要Mock StpUtil静态方法（sa-token），
		// 纯单元测试无法覆盖，需在集成测试中验证。
	}

	@Nested
	@DisplayName("获取当前用户角色测试")
	class RoleTests {

		// 注意：获取角色测试需要Mock StpUtil静态方法（sa-token），
		// 纯单元测试无法覆盖，需在集成测试中验证。
	}
}
