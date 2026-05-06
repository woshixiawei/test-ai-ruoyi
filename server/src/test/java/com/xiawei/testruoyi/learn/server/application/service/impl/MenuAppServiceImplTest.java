package com.xiawei.testruoyi.learn.server.application.service.impl;

import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.server.application.bo.menu.MenuBO;
import com.xiawei.testruoyi.learn.server.application.bo.menu.MenuCreateBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.MenuDO;
import com.xiawei.testruoyi.learn.server.domain.user.service.MenuDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.MenuConvertor;
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
 * 菜单应用服务单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("MenuAppService 单元测试")
class MenuAppServiceImplTest {

	@Mock
	private MenuDomainService menuDomainService;

	@Mock
	private MenuConvertor menuConvertor;

	@InjectMocks
	private MenuAppServiceImpl menuAppService;

	private MenuCreateBO createBO;
	private MenuDO menuDO;
	private MenuBO menuBO;

	@BeforeEach
	void setUp() {
		createBO = new MenuCreateBO();
		createBO.setMenuName("用户管理");
		createBO.setMenuType(0);

		menuDO = new MenuDO();
		menuDO.setId(1L);
		menuDO.setMenuName("用户管理");

		menuBO = new MenuBO();
		menuBO.setId(1L);
		menuBO.setMenuName("用户管理");
	}

	@Nested
	@DisplayName("创建菜单测试")
	class CreateMenuTests {

		@Test
		@DisplayName("参数为null时抛出ParamException")
		void createMenu_nullParam_throwsParamException() {
			assertThatThrownBy(() -> menuAppService.createMenu(null))
					.isInstanceOf(ParamException.class)
					.hasMessage("创建参数不能为空");
		}

		@Test
		@DisplayName("菜单名称为空时抛出ParamException")
		void createMenu_emptyName_throwsParamException() {
			createBO.setMenuName("");
			assertThatThrownBy(() -> menuAppService.createMenu(createBO))
					.isInstanceOf(ParamException.class)
					.hasMessage("菜单名称不能为空");
		}

		@Test
		@DisplayName("菜单类型为null时抛出ParamException")
		void createMenu_nullType_throwsParamException() {
			createBO.setMenuType(null);
			assertThatThrownBy(() -> menuAppService.createMenu(createBO))
					.isInstanceOf(ParamException.class)
					.hasMessage("菜单类型不合法");
		}

		@Test
		@DisplayName("菜单类型为非法值3时抛出ParamException")
		void createMenu_invalidType_throwsParamException() {
			createBO.setMenuType(3);
			assertThatThrownBy(() -> menuAppService.createMenu(createBO))
					.isInstanceOf(ParamException.class)
					.hasMessage("菜单类型不合法");
		}

		@Test
		@DisplayName("正常创建菜单返回菜单ID")
		void createMenu_validInput_returnsMenuId() {
			when(menuConvertor.toDomainFromCreateBO(createBO)).thenReturn(menuDO);
			doAnswer(invocation -> {
				MenuDO menu = invocation.getArgument(0);
				menu.setId(1L);
				return null;
			}).when(menuDomainService).save(any(MenuDO.class));

			Long id = menuAppService.createMenu(createBO);
			assertThat(id).isEqualTo(1L);
		}
	}

	@Nested
	@DisplayName("删除菜单测试")
	class DeleteMenuTests {

		@Test
		@DisplayName("ID为null时抛出ParamException")
		void deleteMenu_nullId_throwsParamException() {
			assertThatThrownBy(() -> menuAppService.deleteMenu(null))
					.isInstanceOf(ParamException.class)
					.hasMessage("菜单ID不合法");
		}

		@Test
		@DisplayName("存在子菜单时由DomainService校验")
		void deleteMenu_withChildren_domainServiceValidates() {
			// DomainService.validateNoChildren 抛出异常的场景
			doThrow(new ParamException("存在子菜单，无法删除"))
					.when(menuDomainService).validateNoChildren(1L);

			assertThatThrownBy(() -> menuAppService.deleteMenu(1L))
					.isInstanceOf(ParamException.class)
					.hasMessage("存在子菜单，无法删除");
		}
	}

	@Nested
	@DisplayName("查询菜单测试")
	class QueryMenuTests {

		@Test
		@DisplayName("ID为null时抛出ParamException")
		void getMenuById_nullId_throwsParamException() {
			assertThatThrownBy(() -> menuAppService.getMenuById(null))
					.isInstanceOf(ParamException.class)
					.hasMessage("菜单ID不合法");
		}

		@Test
		@DisplayName("正常获取菜单树")
		void menuTree_returnsTree() {
			when(menuDomainService.findAll()).thenReturn(List.of(menuDO));
			when(menuDomainService.buildTree(anyList())).thenReturn(List.of(menuDO));
			when(menuConvertor.toBOList(anyList())).thenReturn(List.of(menuBO));

			List<MenuBO> result = menuAppService.menuTree();
			assertThat(result).hasSize(1);
		}
	}
}
