package com.xiawei.testruoyi.learn.server.application.bo.dict;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 字典类型业务对象
 */
@Data
public class DictTypeBO {

	/** 主键 */
	private Long id;

	/** 字典名称 */
	private String dictName;

	/** 字典类型编码 */
	private String dictType;

	/** 状态 */
	private Integer status;

	/** 备注 */
	private String remark;

	/** 创建时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date createTime;

	/** 更新时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date updateTime;
}
