package com.xiawei.testruoyi.learn.server.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiawei.testruoyi.learn.base.basecls.BaseRepositoryImpl;
import com.xiawei.testruoyi.learn.server.domain.user.repository.MenuRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.mapper.MenuMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.mapper.RoleMenuMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.MenuPO;
import com.xiawei.testruoyi.learn.server.infrastructure.po.RoleMenuPO;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 菜单仓储实现
 */
@Repository
public class MenuRepositoryImpl extends BaseRepositoryImpl<MenuMapper, MenuPO> implements MenuRepository {

	private final RoleMenuMapper roleMenuMapper;

	public MenuRepositoryImpl(RoleMenuMapper roleMenuMapper) {
		this.roleMenuMapper = roleMenuMapper;
	}

	@Override
	public List<MenuPO> findAllOrder() {
		return lambdaQuery().orderByAsc(MenuPO::getSort).list();
	}

	@Override
	public List<MenuPO> findByRoleIds(List<Long> roleIds) {
		if (CollectionUtils.isEmpty(roleIds)) {
			return List.of();
		}
		// 查询角色菜单关联
		List<RoleMenuPO> roleMenus = roleMenuMapper.selectList(
				Wrappers.lambdaQuery(RoleMenuPO.class).in(RoleMenuPO::getRoleId, roleIds)
		);
		if (CollectionUtils.isEmpty(roleMenus)) {
			return List.of();
		}
		List<Long> menuIds = roleMenus.stream().map(RoleMenuPO::getMenuId).distinct().toList();
		return listByIds(menuIds);
	}

	@Override
	public boolean existsByParentId(Long parentId) {
		return lambdaQuery().eq(MenuPO::getParentId, parentId).exists();
	}
}
