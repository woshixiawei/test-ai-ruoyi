package com.xiawei.testruoyi.learn.sdk.dto.notice;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 通知公告响应DTO
 */
@Data
@Schema(description = "通知公告信息")
public class NoticeDTO {

	@Schema(description = "公告ID")
	private Long id;

	@Schema(description = "公告标题")
	private String title;

	@Schema(description = "公告内容")
	private String content;

	@Schema(description = "类型：1-通知，2-公告")
	private Integer noticeType;

	@Schema(description = "状态：0-草稿，1-已发布，2-已撤回")
	private Integer status;

	@Schema(description = "发布人ID")
	private Long publisherId;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "发布时间")
	private Date publishTime;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "创建时间")
	private Date createTime;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "更新时间")
	private Date updateTime;
}
