package com.xiawei.testruoyi.learn.sdk.dto.config;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 参数配置响应DTO
 */
@Data
@Schema(description = "参数配置信息")
public class ConfigDTO {

	@Schema(description = "参数ID")
	private Long id;

	@Schema(description = "参数名称")
	private String configName;

	@Schema(description = "参数键名")
	private String configKey;

	@Schema(description = "参数键值")
	private String configValue;

	@Schema(description = "是否系统内置")
	private Integer isSystem;

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
