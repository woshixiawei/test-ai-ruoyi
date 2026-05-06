package com.xiawei.testruoyi.learn.sdk.dto.menu;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 菜单响应DTO
 */
@Data
@Schema(description = "菜单信息")
public class MenuDTO {

	@Schema(description = "菜单ID")
	private Long id;

	@Schema(description = "菜单名称")
	private String menuName;

	@Schema(description = "父菜单ID")
	private Long parentId;

	@Schema(description = "显示顺序")
	private Integer sort;

	@Schema(description = "路由地址")
	private String path;

	@Schema(description = "组件路径")
	private String component;

	@Schema(description = "权限标识")
	private String permission;

	@Schema(description = "类型：0-目录，1-菜单，2-按钮")
	private Integer menuType;

	@Schema(description = "菜单图标")
	private String icon;

	@Schema(description = "状态")
	private Integer status;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "创建时间")
	private Date createTime;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "更新时间")
	private Date updateTime;

	@Schema(description = "子菜单列表")
	private List<MenuDTO> children;
}
