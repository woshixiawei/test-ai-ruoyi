package com.xiawei.testruoyi.learn.server.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.basecls.BaseRepositoryImpl;
import com.xiawei.testruoyi.learn.server.domain.user.repository.UserRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.mapper.UserMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.mapper.UserRoleMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.UserPO;
import com.xiawei.testruoyi.learn.server.infrastructure.po.UserRolePO;
import com.xiawei.testruoyi.learn.server.infrastructure.query.UserQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * 用户仓储实现
 *
 * <p>继承 BaseRepositoryImpl<UserMapper, UserPO>，获得 MyBatis-Plus ServiceImpl 的通用 CRUD 实现。</p>
 * <p>自定义查询方法使用 lambdaQuery() / Wrappers.lambdaQuery() 构建，无需 XML 映射文件。</p>
 * <p>关系表（sys_user_role）操作通过 UserRoleMapper + Wrappers.lambdaQuery() 纯 Java 实现。</p>
 */
@Repository
public class UserRepositoryImpl extends BaseRepositoryImpl<UserMapper, UserPO> implements UserRepository {

	private final UserRoleMapper userRoleMapper;

	public UserRepositoryImpl(UserRoleMapper userRoleMapper) {
		this.userRoleMapper = userRoleMapper;
	}

	@Override
	public Optional<UserPO> findByUsername(String username) {
		return lambdaQuery().eq(UserPO::getUsername, username).oneOpt();
	}

	@Override
	public Page<UserPO> findPage(UserQuery query) {
		return lambdaQuery()
				.like(query.getKeyword() != null, UserPO::getUsername, query.getKeyword())
				.or()
				.like(query.getKeyword() != null, UserPO::getNickname, query.getKeyword())
				.or()
				.like(query.getKeyword() != null, UserPO::getEmail, query.getKeyword())
				.or()
				.like(query.getKeyword() != null, UserPO::getPhone, query.getKeyword())
				.eq(query.getStatus() != null, UserPO::getStatus, query.getStatus())
				.orderByDesc(UserPO::getCreateTime)
				.page(new Page<>(query.getCurrent(), query.getSize()));
	}

	@Override
	public List<Long> findRoleIdsByUserId(Long userId) {
		List<UserRolePO> userRoles = userRoleMapper.selectList(
				Wrappers.lambdaQuery(UserRolePO.class).eq(UserRolePO::getUserId, userId)
		);
		return userRoles.stream().map(UserRolePO::getRoleId).toList();
	}

	@Override
	public void assignRoles(Long userId, List<Long> roleIds) {
		// 1. 删除原有角色关联
		userRoleMapper.delete(
				Wrappers.lambdaQuery(UserRolePO.class).eq(UserRolePO::getUserId, userId)
		);
		// 2. 批量插入新角色关联
		if (!CollectionUtils.isEmpty(roleIds)) {
			for (Long roleId : roleIds) {
				userRoleMapper.insert(new UserRolePO(userId, roleId));
			}
		}
	}
}
