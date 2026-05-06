package com.xiawei.testruoyi.learn.server.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiawei.testruoyi.learn.base.basecls.BaseRepositoryImpl;
import com.xiawei.testruoyi.learn.server.domain.user.repository.DeptRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.mapper.DeptMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.mapper.UserMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.DeptPO;
import com.xiawei.testruoyi.learn.server.infrastructure.po.UserPO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门仓储实现
 */
@Repository
public class DeptRepositoryImpl extends BaseRepositoryImpl<DeptMapper, DeptPO> implements DeptRepository {

	private final UserMapper userMapper;

	public DeptRepositoryImpl(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Override
	public List<DeptPO> findAllOrder() {
		return lambdaQuery().orderByAsc(DeptPO::getSort).list();
	}

	@Override
	public boolean existsByParentId(Long parentId) {
		return lambdaQuery().eq(DeptPO::getParentId, parentId).exists();
	}

	@Override
	public boolean existsUserByDeptId(Long deptId) {
		return userMapper.exists(Wrappers.lambdaQuery(UserPO.class).eq(UserPO::getDeptId, deptId));
	}

	@Override
	public List<Long> findChildDeptIds(Long parentId) {
		List<Long> allIds = new ArrayList<>();
		collectChildIds(parentId, allIds);
		return allIds;
	}

	/** 递归收集子部门ID */
	private void collectChildIds(Long parentId, List<Long> result) {
		List<DeptPO> children = lambdaQuery().eq(DeptPO::getParentId, parentId).select(DeptPO::getId).list();
		for (DeptPO child : children) {
			result.add(child.getId());
			collectChildIds(child.getId(), result);
		}
	}
}
