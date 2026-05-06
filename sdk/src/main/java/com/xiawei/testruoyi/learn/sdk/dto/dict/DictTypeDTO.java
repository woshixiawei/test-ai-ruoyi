package com.xiawei.testruoyi.learn.sdk.dto.dict;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 字典类型响应DTO
 */
@Data
@Schema(description = "字典类型信息")
public class DictTypeDTO {

	@Schema(description = "字典类型ID")
	private Long id;

	@Schema(description = "字典名称")
	private String dictName;

	@Schema(description = "字典类型编码")
	private String dictType;

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
