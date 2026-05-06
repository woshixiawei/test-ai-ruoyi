package com.xiawei.testruoyi.learn.sdk.cmd.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 菜单创建/修改命令
 */
@Data
@Schema(description = "菜单创建/修改请求")
public class MenuCmd {

	@Schema(description = "菜单ID（修改时必传）")
	private Long id;

	@NotBlank(message = "菜单名称不能为空")
	@Schema(description = "菜单名称")
	private String menuName;

	@Schema(description = "父菜单ID，0为顶级")
	private Long parentId;

	@Schema(description = "显示顺序")
	private Integer sort;

	@Schema(description = "路由地址")
	private String path;

	@Schema(description = "组件路径")
	private String component;

	@Schema(description = "权限标识")
	private String permission;

	@NotNull(message = "菜单类型不能为空")
	@Schema(description = "类型：0-目录，1-菜单，2-按钮")
	private Integer menuType;

	@Schema(description = "菜单图标")
	private String icon;

	@Schema(description = "状态：0-禁用，1-正常")
	private Integer status;
}
