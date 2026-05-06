package com.xiawei.testruoyi.learn.server.domain.user.service;

import com.xiawei.testruoyi.learn.base.exceptions.BusinessException;
import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.server.domain.user.do_.MenuDO;
import com.xiawei.testruoyi.learn.server.domain.user.repository.MenuRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.MenuConvertor;
import com.xiawei.testruoyi.learn.server.infrastructure.po.MenuPO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单领域服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MenuDomainService {

	private final MenuRepository menuRepository;
	private final MenuConvertor menuConvertor;

	/** 保存菜单 */
	public void save(MenuDO menu) {
		MenuPO po = menuConvertor.toPO(menu);
		menuRepository.saveOrUpdate(po);
		menu.setId(po.getId());
	}

	/** 根据ID查询菜单 */
	public MenuDO getById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("菜单ID不合法");
		}
		MenuPO po = menuRepository.getById(id);
		if (po == null) {
			throw new BusinessException("菜单不存在");
		}
		return menuConvertor.toDomain(po);
	}

	/** 校验菜单存在 */
	public MenuDO validateMenuExists(Long menuId) {
		if (menuId == null || menuId <= 0) {
			throw new ParamException("菜单ID不合法");
		}
		MenuPO po = menuRepository.getById(menuId);
		if (po == null) {
			throw new BusinessException("菜单不存在");
		}
		return menuConvertor.toDomain(po);
	}

	/** 校验菜单无子菜单（业务规则校验） */
	public void validateNoChildren(Long menuId) {
		if (menuId == null || menuId <= 0) {
			throw new ParamException("菜单ID不合法");
		}
		if (menuRepository.existsByParentId(menuId)) {
			throw new BusinessException("存在子菜单，禁止删除");
		}
	}

	/** 删除菜单 */
	public void deleteById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("菜单ID不合法");
		}
		menuRepository.removeById(id);
	}

	/** 查询所有菜单列表 */
	public List<MenuDO> findAll() {
		List<MenuPO> pos = menuRepository.findAllOrder();
		return menuConvertor.toDomainList(pos);
	}

	/** 构建菜单树 */
	public List<MenuDO> buildTree(List<MenuDO> menus) {
		Map<Long, List<MenuDO>> groupByParent = menus.stream()
				.collect(Collectors.groupingBy(MenuDO::getParentId));
		menus.forEach(m -> m.setChildren(groupByParent.getOrDefault(m.getId(), new ArrayList<>())));
		return menus.stream().filter(m -> m.getParentId() == 0L).collect(Collectors.toList());
	}

	/** 根据角色ID列表查询菜单 */
	public List<MenuDO> findByRoleIds(List<Long> roleIds) {
		if (roleIds == null || roleIds.isEmpty()) {
			return List.of();
		}
		List<MenuPO> pos = menuRepository.findByRoleIds(roleIds);
		return menuConvertor.toDomainList(pos);
	}
}
