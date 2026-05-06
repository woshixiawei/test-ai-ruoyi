package com.xiawei.testruoyi.learn.sdk.cmd.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 字典数据命令
 */
@Data
@Schema(description = "字典数据命令")
public class DictDataCmd {

	@Schema(description = "字典数据ID（更新时必填）")
	private Long id;

	@NotNull(message = "字典类型ID不能为空")
	@Schema(description = "字典类型ID")
	private Long dictTypeId;

	@NotBlank(message = "数据标签不能为空")
	@Schema(description = "数据标签")
	private String dictLabel;

	@NotBlank(message = "数据键值不能为空")
	@Schema(description = "数据键值")
	private String dictValue;

	@Schema(description = "显示顺序")
	private Integer sort;

	@Schema(description = "状态：0-禁用，1-正常")
	private Integer status;

	@Schema(description = "备注")
	private String remark;
}
