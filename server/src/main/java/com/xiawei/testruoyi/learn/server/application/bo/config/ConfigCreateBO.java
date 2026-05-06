package com.xiawei.testruoyi.learn.server.application.bo.config;

import lombok.Data;

/**
 * 参数配置创建业务对象
 */
@Data
public class ConfigCreateBO {

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
}
