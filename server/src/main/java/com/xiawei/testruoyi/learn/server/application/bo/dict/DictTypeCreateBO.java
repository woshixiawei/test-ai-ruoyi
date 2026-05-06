package com.xiawei.testruoyi.learn.server.application.bo.dict;

import lombok.Data;

/**
 * 字典类型创建/修改业务对象
 */
@Data
public class DictTypeCreateBO {

	/** 字典名称 */
	private String dictName;

	/** 字典类型编码 */
	private String dictType;

	/** 状态 */
	private Integer status;

	/** 备注 */
	private String remark;
}
