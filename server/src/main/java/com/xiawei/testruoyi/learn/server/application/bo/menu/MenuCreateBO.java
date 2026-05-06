package com.xiawei.testruoyi.learn.server.application.bo.menu;

import lombok.Data;

/**
 * 菜单创建/修改业务对象
 */
@Data
public class MenuCreateBO {

	/** 菜单名称 */
	private String menuName;

	/** 父菜单ID */
	private Long parentId;

	/** 菜单类型 */
	private Integer menuType;

	/** 路由路径 */
	private String path;

	/** 组件路径 */
	private String component;

	/** 权限标识 */
	private String permission;

	/** 图标 */
	private String icon;

	/** 显示顺序 */
	private Integer sort;

	/** 状态 */
	private Integer status;
}
