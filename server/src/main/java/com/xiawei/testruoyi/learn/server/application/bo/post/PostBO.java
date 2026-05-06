package com.xiawei.testruoyi.learn.server.application.bo.post;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 岗位业务对象
 */
@Data
public class PostBO {

	/** 主键 */
	private Long id;

	/** 岗位编码 */
	private String postCode;

	/** 岗位名称 */
	private String postName;

	/** 显示顺序 */
	private Integer sort;

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
