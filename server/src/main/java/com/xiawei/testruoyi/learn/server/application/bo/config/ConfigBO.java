package com.xiawei.testruoyi.learn.server.application.bo.config;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 参数配置业务对象
 */
@Data
public class ConfigBO {

	/** 主键 */
	private Long id;

	/** 参数名称 */
	private String configName;

	/** 参数键名 */
	private String configKey;

	/** 参数键值 */
	private String configValue;

	/** 是否系统内置：0-否，1-是 */
	private Integer isSystem;

	/** 备注 */
	private String remark;

	/** 创建时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date createTime;

	/** 更新时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date updateTime;
}
