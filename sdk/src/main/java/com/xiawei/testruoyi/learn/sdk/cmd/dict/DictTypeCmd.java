package com.xiawei.testruoyi.learn.sdk.cmd.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 字典类型命令
 */
@Data
@Schema(description = "字典类型命令")
public class DictTypeCmd {

	@Schema(description = "字典类型ID（更新时必填）")
	private Long id;

	@NotBlank(message = "字典名称不能为空")
	@Schema(description = "字典名称")
	private String dictName;

	@NotBlank(message = "字典类型编码不能为空")
	@Schema(description = "字典类型编码")
	private String dictType;

	@Schema(description = "状态：0-禁用，1-正常")
	private Integer status;

	@Schema(description = "备注")
	private String remark;
}
