package com.xiawei.testruoyi.learn.server.application.bo.menu;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 菜单业务对象
 */
@Data
public class MenuBO {

	/** 主键 */
	private Long id;

	/** 菜单名称 */
	private String menuName;

	/** 父菜单ID */
	private Long parentId;

	/** 菜单类型：0-目录，1-菜单，2-按钮 */
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

	/** 状态：0-禁用，1-正常 */
	private Integer status;

	/** 子菜单列表 */
	private List<MenuBO> children;

	/** 创建时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date createTime;

	/** 更新时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date updateTime;
}
