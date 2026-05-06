package com.xiawei.testruoyi.learn.server.application.bo.dict;

import lombok.Data;

/**
 * 字典数据创建/修改业务对象
 */
@Data
public class DictDataCreateBO {

	/** 字典类型ID */
	private Long dictTypeId;

	/** 数据标签 */
	private String dictLabel;

	/** 数据键值 */
	private String dictValue;

	/** 显示顺序 */
	private Integer sort;

	/** 状态 */
	private Integer status;

	/** 备注 */
	private String remark;
}
