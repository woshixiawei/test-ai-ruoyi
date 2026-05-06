package com.xiawei.testruoyi.learn.server.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.basecls.BaseRepositoryImpl;
import com.xiawei.testruoyi.learn.server.domain.user.repository.RoleRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.mapper.RoleMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.mapper.RoleMenuMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.mapper.UserRoleMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.RoleMenuPO;
import com.xiawei.testruoyi.learn.server.infrastructure.po.RolePO;
import com.xiawei.testruoyi.learn.server.infrastructure.po.UserRolePO;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 角色仓储实现
 */
@Repository
public class RoleRepositoryImpl extends BaseRepositoryImpl<RoleMapper, RolePO> implements RoleRepository {

	private final RoleMenuMapper roleMenuMapper;
	private final UserRoleMapper userRoleMapper;

	public RoleRepositoryImpl(RoleMenuMapper roleMenuMapper, UserRoleMapper userRoleMapper) {
		this.roleMenuMapper = roleMenuMapper;
		this.userRoleMapper = userRoleMapper;
	}

	@Override
	public RolePO findByRoleCode(String roleCode) {
		return lambdaQuery().eq(RolePO::getRoleCode, roleCode).one();
	}

	@Override
	public Page<RolePO> findPage(long current, long size, String keyword, Integer status) {
		return lambdaQuery()
				.like(keyword != null, RolePO::getRoleName, keyword)
				.or()
				.like(keyword != null, RolePO::getRoleCode, keyword)
				.eq(status != null, RolePO::getStatus, status)
				.orderByAsc(RolePO::getSort)
				.page(new Page<>(current, size));
	}

	@Override
	public List<Long> findMenuIdsByRoleId(Long roleId) {
		List<RoleMenuPO> roleMenus = roleMenuMapper.selectList(
				Wrappers.lambdaQuery(RoleMenuPO.class).eq(RoleMenuPO::getRoleId, roleId)
		);
		return roleMenus.stream().map(RoleMenuPO::getMenuId).toList();
	}

	@Override
	public void assignMenus(Long roleId, List<Long> menuIds) {
		// 1. 删除原有菜单关联
		roleMenuMapper.delete(
				Wrappers.lambdaQuery(RoleMenuPO.class).eq(RoleMenuPO::getRoleId, roleId)
		);
		// 2. 批量插入新菜单关联
		if (!CollectionUtils.isEmpty(menuIds)) {
			for (Long menuId : menuIds) {
				RoleMenuPO po = new RoleMenuPO();
				po.setRoleId(roleId);
				po.setMenuId(menuId);
				roleMenuMapper.insert(po);
			}
		}
	}

	@Override
	public List<RolePO> findByUserId(Long userId) {
		// 通过用户角色关联表查询角色ID列表
		List<UserRolePO> userRoles = userRoleMapper.selectList(
				Wrappers.lambdaQuery(UserRolePO.class).eq(UserRolePO::getUserId, userId)
		);
		if (CollectionUtils.isEmpty(userRoles)) {
			return List.of();
		}
		List<Long> roleIds = userRoles.stream().map(UserRolePO::getRoleId).toList();
		return listByIds(roleIds);
	}
}
