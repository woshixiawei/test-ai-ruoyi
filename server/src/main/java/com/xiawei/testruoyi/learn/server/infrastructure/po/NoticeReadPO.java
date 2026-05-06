package com.xiawei.testruoyi.learn.server.infrastructure.po;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 公告已读记录持久化对象
 */
@Data
@Accessors(chain = true)
@TableName("sys_notice_read")
public class NoticeReadPO {

	/** 主键 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/** 公告ID */
	private Long noticeId;

	/** 用户ID */
	private Long userId;

	/** 阅读时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date readTime;

	/** 创建时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date createTime;

	/** 更新时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date updateTime;
}
