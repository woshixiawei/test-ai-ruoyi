package com.xiawei.testruoyi.learn.server.application.service.impl;

import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.server.application.bo.role.RoleBO;
import com.xiawei.testruoyi.learn.server.application.bo.role.RoleCreateBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.RoleDO;
import com.xiawei.testruoyi.learn.server.domain.user.service.RoleDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.RoleConvertor;
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
 * 角色应用服务单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RoleAppService 单元测试")
class RoleAppServiceImplTest {

	@Mock
	private RoleDomainService roleDomainService;

	@Mock
	private RoleConvertor roleConvertor;

	@InjectMocks
	private RoleAppServiceImpl roleAppService;

	private RoleCreateBO createBO;
	private RoleDO roleDO;
	private RoleBO roleBO;

	@BeforeEach
	void setUp() {
		createBO = new RoleCreateBO();
		createBO.setRoleName("管理员");
		createBO.setRoleCode("admin");
		createBO.setSort(1);

		roleDO = new RoleDO();
		roleDO.setId(1L);
		roleDO.setRoleName("管理员");
		roleDO.setRoleCode("admin");

		roleBO = new RoleBO();
		roleBO.setId(1L);
		roleBO.setRoleName("管理员");
	}

	@Nested
	@DisplayName("创建角色测试")
	class CreateRoleTests {

		@Test
		@DisplayName("参数为null时抛出ParamException")
		void createRole_nullParam_throwsParamException() {
			assertThatThrownBy(() -> roleAppService.createRole(null))
					.isInstanceOf(ParamException.class)
					.hasMessage("创建参数不能为空");
		}

		@Test
		@DisplayName("角色名称为空时抛出ParamException")
		void createRole_emptyName_throwsParamException() {
			createBO.setRoleName("");
			assertThatThrownBy(() -> roleAppService.createRole(createBO))
					.isInstanceOf(ParamException.class)
					.hasMessage("角色名称不能为空");
		}

		@Test
		@DisplayName("角色编码为空时抛出ParamException")
		void createRole_emptyCode_throwsParamException() {
			createBO.setRoleCode("");
			assertThatThrownBy(() -> roleAppService.createRole(createBO))
					.isInstanceOf(ParamException.class)
					.hasMessage("角色编码不能为空");
		}

		@Test
		@DisplayName("正常创建角色返回角色ID")
		void createRole_validInput_returnsRoleId() {
			when(roleConvertor.toDomainFromCreateBO(createBO)).thenReturn(roleDO);
			doAnswer(invocation -> {
				RoleDO role = invocation.getArgument(0);
				role.setId(1L);
				return null;
			}).when(roleDomainService).save(any(RoleDO.class));

			Long id = roleAppService.createRole(createBO);
			assertThat(id).isEqualTo(1L);
			verify(roleDomainService).validateRoleCodeNotExists("admin");
		}
	}

	@Nested
	@DisplayName("删除角色测试")
	class DeleteRoleTests {

		@Test
		@DisplayName("ID为null时抛出ParamException")
		void deleteRole_nullId_throwsParamException() {
			assertThatThrownBy(() -> roleAppService.deleteRole(null))
					.isInstanceOf(ParamException.class)
					.hasMessage("角色ID不合法");
		}

		@Test
		@DisplayName("ID为负数时抛出ParamException")
		void deleteRole_negativeId_throwsParamException() {
			assertThatThrownBy(() -> roleAppService.deleteRole(-1L))
					.isInstanceOf(ParamException.class)
					.hasMessage("角色ID不合法");
		}

		@Test
		@DisplayName("正常删除角色")
		void deleteRole_validId_deletesSuccessfully() {
			when(roleDomainService.validateRoleExists(2L)).thenReturn(roleDO);

			roleAppService.deleteRole(2L);
			verify(roleDomainService).deleteById(2L);
		}
	}

	@Nested
	@DisplayName("获取角色菜单测试")
	class GetRoleMenusTests {

		@Test
		@DisplayName("ID为null时抛出ParamException")
		void getRoleMenus_nullId_throwsParamException() {
			assertThatThrownBy(() -> roleAppService.getRoleMenus(null))
					.isInstanceOf(ParamException.class)
					.hasMessage("角色ID不合法");
		}

		@Test
		@DisplayName("正常获取角色菜单ID列表")
		void getRoleMenus_validId_returnsMenuIds() {
			when(roleDomainService.getMenuIdsByRoleId(1L)).thenReturn(List.of(1L, 2L, 3L));

			List<Long> menuIds = roleAppService.getRoleMenus(1L);
			assertThat(menuIds).hasSize(3);
			assertThat(menuIds).containsExactly(1L, 2L, 3L);
		}
	}

	@Nested
	@DisplayName("分配角色菜单测试")
	class AssignRoleMenusTests {

		@Test
		@DisplayName("ID为null时抛出ParamException")
		void assignRoleMenus_nullId_throwsParamException() {
			assertThatThrownBy(() -> roleAppService.assignRoleMenus(null, List.of(1L)))
					.isInstanceOf(ParamException.class)
					.hasMessage("角色ID不合法");
		}

		@Test
		@DisplayName("正常分配角色菜单")
		void assignRoleMenus_validInput_assignsSuccessfully() {
			when(roleDomainService.validateRoleExists(1L)).thenReturn(roleDO);

			roleAppService.assignRoleMenus(1L, List.of(1L, 2L));
			verify(roleDomainService).assignMenus(1L, List.of(1L, 2L));
		}
	}

	@Nested
	@DisplayName("查询角色测试")
	class QueryRoleTests {

		@Test
		@DisplayName("根据ID查询返回RoleBO")
		void getRoleById_validId_returnsRoleBO() {
			when(roleDomainService.getById(1L)).thenReturn(roleDO);
			when(roleConvertor.toBO(roleDO)).thenReturn(roleBO);

			RoleBO result = roleAppService.getRoleById(1L);
			assertThat(result).isNotNull();
			assertThat(result.getId()).isEqualTo(1L);
		}
	}
}
