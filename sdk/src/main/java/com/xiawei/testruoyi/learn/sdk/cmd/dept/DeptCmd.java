package com.xiawei.testruoyi.learn.sdk.cmd.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 部门命令
 */
@Data
@Schema(description = "部门命令")
public class DeptCmd {

	@Schema(description = "部门ID（更新时必填）")
	private Long id;

	@NotBlank(message = "部门名称不能为空")
	@Schema(description = "部门名称")
	private String deptName;

	@NotNull(message = "父部门ID不能为空")
	@Schema(description = "父部门ID")
	private Long parentId;

	@Schema(description = "显示顺序")
	private Integer sort;

	@Schema(description = "负责人")
	private String leader;

	@Schema(description = "联系电话")
	private String phone;

	@Schema(description = "邮箱")
	private String email;

	@Schema(description = "状态：0-禁用，1-正常")
	private Integer status;
}
