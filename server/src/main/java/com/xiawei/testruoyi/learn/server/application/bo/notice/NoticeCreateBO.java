package com.xiawei.testruoyi.learn.server.application.bo.notice;

import lombok.Data;

/**
 * 通知公告创建业务对象
 */
@Data
public class NoticeCreateBO {

	/** 公告标题 */
	private String title;

	/** 公告内容 */
	private String content;

	/** 类型：1-通知，2-公告 */
	private Integer noticeType;

	/** 状态：0-草稿，1-已发布，2-已撤回 */
	private Integer status;
}
