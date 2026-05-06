package com.xiawei.testruoyi.learn.server.application.bo.dict;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 字典数据业务对象
 */
@Data
public class DictDataBO {

	/** 主键 */
	private Long id;

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

	/** 创建时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date createTime;

	/** 更新时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date updateTime;
}
