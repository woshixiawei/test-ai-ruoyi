package com.xiawei.testruoyi.learn.server.application.service;

import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.server.application.bo.user.UserBO;
import com.xiawei.testruoyi.learn.server.application.bo.user.UserCreateBO;

/**
 * 用户应用服务接口
 *
 * <p>入参和出参对象类型必须以 BO 结尾，禁止暴露Cmd/DTO等其他类型</p>
 */
public interface UserAppService {

	/**
	 * 创建用户
	 */
	Long createUser(UserCreateBO bo);

	/**
	 * 更新用户
	 */
	void updateUser(Long id, UserCreateBO bo);

	/**
	 * 删除用户
	 */
	void deleteUser(Long id);

	/**
	 * 根据ID查询用户
	 */
	UserBO getUserById(Long id);

	/**
	 * 根据用户名查询用户（供认证应用服务调用）
	 */
	UserBO getUserByUsername(String username);

	/**
	 * 分页查询用户列表
	 */
	PageBO<UserBO> pageUsers(long current, long size, String keyword, Integer status);

	/**
	 * 重置用户密码
	 */
	void resetPassword(Long id, String newPassword);

	/**
	 * 修改用户状态
	 */
	void updateStatus(Long id, Integer status);
}
