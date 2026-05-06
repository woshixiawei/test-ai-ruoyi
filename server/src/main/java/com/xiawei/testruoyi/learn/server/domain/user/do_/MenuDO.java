package com.xiawei.testruoyi.learn.server.domain.user.do_;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 菜单领域数据对象
 */
@Data
public class MenuDO {

	private Long id;
	private String menuName;
	private Long parentId;
	private Integer sort;
	private String path;
	private String component;
	private String permission;
	private Integer menuType;
	private String icon;
	private Integer status;

	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date createTime;

	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date updateTime;

	/** 子菜单列表（树形结构） */
	private List<MenuDO> children;
}
