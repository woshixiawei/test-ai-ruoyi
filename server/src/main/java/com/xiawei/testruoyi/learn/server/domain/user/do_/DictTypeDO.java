package com.xiawei.testruoyi.learn.server.domain.user.do_;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 字典类型领域数据对象
 */
@Data
public class DictTypeDO {

	/** 主键 */
	private Long id;

	/** 字典名称 */
	private String dictName;

	/** 字典类型编码，唯一 */
	private String dictType;

	/** 状态：0-禁用，1-正常 */
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
