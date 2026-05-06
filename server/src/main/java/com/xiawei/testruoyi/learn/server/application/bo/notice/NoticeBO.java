package com.xiawei.testruoyi.learn.server.application.bo.notice;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 通知公告业务对象
 */
@Data
public class NoticeBO {

	/** 主键 */
	private Long id;

	/** 公告标题 */
	private String title;

	/** 公告内容 */
	private String content;

	/** 类型：1-通知，2-公告 */
	private Integer noticeType;

	/** 状态：0-草稿，1-已发布，2-已撤回 */
	private Integer status;

	/** 发布人ID */
	private Long publisherId;

	/** 发布时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date publishTime;

	/** 创建时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date createTime;

	/** 更新时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date updateTime;
}
