package com.xiawei.testruoyi.learn.server.domain.user.repository;

import com.xiawei.testruoyi.learn.base.basecls.BaseRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.po.DeptPO;

import java.util.List;

/**
 * 部门仓储接口
 */
public interface DeptRepository extends BaseRepository<DeptPO> {

	/**
	 * 查询所有部门（按排序）
	 */
	List<DeptPO> findAllOrder();

	/**
	 * 查询子部门是否存在
	 */
	boolean existsByParentId(Long parentId);

	/**
	 * 查询部门下是否存在用户
	 */
	boolean existsUserByDeptId(Long deptId);

	/**
	 * 查询指定部门的所有子部门ID（递归）
	 */
	List<Long> findChildDeptIds(Long parentId);
}
