package com.xiawei.testruoyi.learn.server.domain.user.repository;

import com.xiawei.testruoyi.learn.base.basecls.BaseRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.po.UserPO;
import com.xiawei.testruoyi.learn.server.infrastructure.query.UserQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Optional;

/**
 * 用户仓储接口
 *
 * <p>继承 BaseRepository<UserPO>，获得 MyBatis-Plus IService 的通用 CRUD 能力。</p>
 * <p>自定义查询方法使用 LambdaQueryWrapper 构建。</p>
 */
public interface UserRepository extends BaseRepository<UserPO> {

	/**
	 * 根据用户名查询用户
	 */
	Optional<UserPO> findByUsername(String username);

	/**
	 * 分页查询用户
	 */
	Page<UserPO> findPage(UserQuery query);

	/**
	 * 查询用户的角色ID列表
	 */
	List<Long> findRoleIdsByUserId(Long userId);

	/**
	 * 分配角色（关联表操作）
	 */
	void assignRoles(Long userId, List<Long> roleIds);
}
