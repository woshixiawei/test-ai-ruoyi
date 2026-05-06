package com.xiawei.testruoyi.learn.server.domain.user.repository;

import com.xiawei.testruoyi.learn.base.basecls.BaseRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.po.MenuPO;

import java.util.List;

/**
 * 菜单仓储接口
 */
public interface MenuRepository extends BaseRepository<MenuPO> {

	/**
	 * 查询所有菜单（按排序）
	 */
	List<MenuPO> findAllOrder();

	/**
	 * 根据角色ID列表查询菜单
	 */
	List<MenuPO> findByRoleIds(List<Long> roleIds);

	/**
	 * 查询子菜单是否存在
	 */
	boolean existsByParentId(Long parentId);
}
