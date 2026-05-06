package com.xiawei.testruoyi.learn.sdk.cmd.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 角色创建/修改命令
 */
@Data
@Schema(description = "角色创建/修改请求")
public class RoleCmd {

	@Schema(description = "角色ID（修改时必传）")
	private Long id;

	@NotBlank(message = "角色名称不能为空")
	@Schema(description = "角色名称")
	private String roleName;

	@NotBlank(message = "角色编码不能为空")
	@Schema(description = "角色编码")
	private String roleCode;

	@NotNull(message = "显示顺序不能为空")
	@Schema(description = "显示顺序")
	private Integer sort;

	@Schema(description = "状态：0-禁用，1-正常")
	private Integer status;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "菜单ID列表")
	private List<Long> menuIds;
}
