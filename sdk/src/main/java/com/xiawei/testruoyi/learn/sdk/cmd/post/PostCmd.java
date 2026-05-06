package com.xiawei.testruoyi.learn.sdk.cmd.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 岗位命令
 */
@Data
@Schema(description = "岗位命令")
public class PostCmd {

	@Schema(description = "岗位ID（更新时必填）")
	private Long id;

	@NotBlank(message = "岗位编码不能为空")
	@Schema(description = "岗位编码")
	private String postCode;

	@NotBlank(message = "岗位名称不能为空")
	@Schema(description = "岗位名称")
	private String postName;

	@Schema(description = "显示顺序")
	private Integer sort;

	@Schema(description = "状态：0-禁用，1-正常")
	private Integer status;

	@Schema(description = "备注")
	private String remark;
}
