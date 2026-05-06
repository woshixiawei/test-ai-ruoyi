package com.xiawei.testruoyi.learn.sdk.dto.dict;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 字典数据响应DTO
 */
@Data
@Schema(description = "字典数据信息")
public class DictDataDTO {

	@Schema(description = "字典数据ID")
	private Long id;

	@Schema(description = "字典类型ID")
	private Long dictTypeId;

	@Schema(description = "数据标签")
	private String dictLabel;

	@Schema(description = "数据键值")
	private String dictValue;

	@Schema(description = "显示顺序")
	private Integer sort;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "备注")
	private String remark;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "创建时间")
	private Date createTime;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "更新时间")
	private Date updateTime;
}
