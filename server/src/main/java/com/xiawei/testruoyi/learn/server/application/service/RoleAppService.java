package com.xiawei.testruoyi.learn.server.application.service;

import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.server.application.bo.role.RoleBO;
import com.xiawei.testruoyi.learn.server.application.bo.role.RoleCreateBO;

import java.util.List;

/**
 * 角色应用服务接口
 */
public interface RoleAppService {

	Long createRole(RoleCreateBO bo);

	void updateRole(Long id, RoleCreateBO bo);

	void deleteRole(Long id);

	RoleBO getRoleById(Long id);

	PageBO<RoleBO> pageRoles(long current, long size, String keyword, Integer status);

	List<Long> getRoleMenus(Long roleId);

	void assignRoleMenus(Long roleId, List<Long> menuIds);
}
