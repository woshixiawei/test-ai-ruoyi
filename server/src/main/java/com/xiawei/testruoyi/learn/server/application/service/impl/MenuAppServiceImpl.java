package com.xiawei.testruoyi.learn.server.application.service.impl;

import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.server.application.service.MenuAppService;
import com.xiawei.testruoyi.learn.server.application.bo.menu.MenuBO;
import com.xiawei.testruoyi.learn.server.application.bo.menu.MenuCreateBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.MenuDO;
import com.xiawei.testruoyi.learn.server.domain.user.service.MenuDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.MenuConvertor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜单应用服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MenuAppServiceImpl implements MenuAppService {

	private final MenuDomainService menuDomainService;
	private final MenuConvertor menuConvertor;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long createMenu(MenuCreateBO bo) {
		validateCreate(bo);
		MenuDO menu = menuConvertor.toDomainFromCreateBO(bo);
		if (menu.getStatus() == null) {
			menu.setStatus(1);
		}
		menuDomainService.save(menu);
		log.info("[MenuAppService] 菜单创建成功, menuId={}", menu.getId());
		return menu.getId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMenu(Long id, MenuCreateBO bo) {
		validateUpdate(id, bo);
		MenuDO menu = menuDomainService.validateMenuExists(id);
		// 使用MapStruct更新DO（禁止手动setter）
		menuConvertor.updateDomainFromBO(bo, menu);
		menuDomainService.save(menu);
		log.info("[MenuAppService] 菜单更新成功, menuId={}", id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteMenu(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("菜单ID不合法");
		}
		menuDomainService.validateNoChildren(id);
		menuDomainService.deleteById(id);
		log.info("[MenuAppService] 菜单删除成功, menuId={}", id);
	}

	@Override
	public MenuBO getMenuById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("菜单ID不合法");
		}
		return menuConvertor.toBO(menuDomainService.getById(id));
	}

	@Override
	public List<MenuBO> listMenus() {
		List<MenuDO> menus = menuDomainService.findAll();
		return menuConvertor.toBOList(menus);
	}

	@Override
	public List<MenuBO> menuTree() {
		List<MenuDO> menus = menuDomainService.findAll();
		List<MenuDO> tree = menuDomainService.buildTree(menus);
		return menuConvertor.toBOList(tree);
	}

	/**
	 * 创建菜单参数校验（应用层：基本格式校验）
	 */
	private void validateCreate(MenuCreateBO bo) {
		if (bo == null) {
			throw new ParamException("创建参数不能为空");
		}
		if (bo.getMenuName() == null || bo.getMenuName().isBlank()) {
			throw new ParamException("菜单名称不能为空");
		}
		if (bo.getMenuType() == null || (bo.getMenuType() != 0 && bo.getMenuType() != 1 && bo.getMenuType() != 2)) {
			throw new ParamException("菜单类型不合法");
		}
	}

	/**
	 * 更新菜单参数校验（应用层：基本格式校验）
	 */
	private void validateUpdate(Long id, MenuCreateBO bo) {
		if (id == null || id <= 0) {
			throw new ParamException("菜单ID不合法");
		}
		validateCreate(bo);
	}
}
