package com.xiawei.testruoyi.learn.server.application.service;

import com.xiawei.testruoyi.learn.server.application.bo.auth.LoginBO;
import com.xiawei.testruoyi.learn.server.application.bo.auth.LoginResultBO;
import com.xiawei.testruoyi.learn.server.application.bo.menu.MenuBO;
import com.xiawei.testruoyi.learn.server.application.bo.user.UserBO;

import java.util.List;
import java.util.Map;

/**
 * 认证应用服务接口
 *
 * <p>入参和出参对象类型必须以 BO 结尾，禁止暴露Cmd/DTO等其他类型</p>
 */
public interface AuthAppService {

	/**
	 * 用户登录
	 */
	LoginResultBO login(LoginBO bo);

	/**
	 * 用户登出
	 */
	void logout();

	/**
	 * 获取当前登录用户信息
	 */
	UserBO getCurrentUserInfo();

	/**
	 * 获取当前用户路由/菜单
	 */
	List<MenuBO> getCurrentUserRoutes();

	/**
	 * 获取当前用户权限标识
	 */
	List<String> getCurrentUserPermissions();

	/**
	 * 获取当前用户角色标识
	 */
	List<String> getCurrentUserRoles();
}
