package com.xiawei.testruoyi.learn.server.domain.user.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.basecls.BaseRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.po.RolePO;

import java.util.List;

/**
 * 角色仓储接口
 */
public interface RoleRepository extends BaseRepository<RolePO> {

	/**
	 * 根据角色编码查询角色
	 */
	RolePO findByRoleCode(String roleCode);

	/**
	 * 分页查询角色
	 */
	Page<RolePO> findPage(long current, long size, String keyword, Integer status);

	/**
	 * 查询角色的菜单ID列表
	 */
	List<Long> findMenuIdsByRoleId(Long roleId);

	/**
	 * 分配角色菜单
	 */
	void assignMenus(Long roleId, List<Long> menuIds);

	/**
	 * 根据用户ID查询角色列表
	 */
	List<RolePO> findByUserId(Long userId);
}
