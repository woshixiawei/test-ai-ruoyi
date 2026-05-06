package com.xiawei.testruoyi.learn.sdk.cmd.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 通知公告命令
 */
@Data
@Schema(description = "通知公告命令")
public class NoticeCmd {

	@Schema(description = "公告ID（更新时必填）")
	private Long id;

	@NotBlank(message = "公告标题不能为空")
	@Schema(description = "公告标题")
	private String title;

	@Schema(description = "公告内容")
	private String content;

	@Schema(description = "类型：1-通知，2-公告")
	private Integer noticeType;

	@Schema(description = "状态：0-草稿，1-已发布，2-已撤回")
	private Integer status;
}
