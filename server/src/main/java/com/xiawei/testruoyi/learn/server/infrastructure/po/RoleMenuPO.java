package com.xiawei.testruoyi.learn.server.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色菜单关联持久化对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role_menu")
public class RoleMenuPO {

	/** 角色ID */
	private Long roleId;

	/** 菜单ID */
	private Long menuId;
}
