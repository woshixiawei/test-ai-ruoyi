package com.xiawei.testruoyi.learn.sdk.cmd.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 参数配置命令
 */
@Data
@Schema(description = "参数配置命令")
public class ConfigCmd {

	@Schema(description = "参数ID（更新时必填）")
	private Long id;

	@NotBlank(message = "参数名称不能为空")
	@Schema(description = "参数名称")
	private String configName;

	@NotBlank(message = "参数键名不能为空")
	@Schema(description = "参数键名")
	private String configKey;

	@Schema(description = "参数键值")
	private String configValue;

	@Schema(description = "是否系统内置：0-否，1-是")
	private Integer isSystem;

	@Schema(description = "备注")
	private String remark;
}
