package com.xiawei.testruoyi.learn.server.application.service;

import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.server.application.bo.notice.NoticeBO;
import com.xiawei.testruoyi.learn.server.application.bo.notice.NoticeCreateBO;

import java.util.List;

/**
 * 通知公告应用服务接口
 */
public interface NoticeAppService {

	/** 新增通知公告 */
	Long createNotice(NoticeCreateBO bo);

	/** 修改通知公告 */
	void updateNotice(Long id, NoticeCreateBO bo);

	/** 删除通知公告 */
	void deleteNotice(Long id);

	/** 通知公告详情 */
	NoticeBO getNoticeById(Long id);

	/** 通知公告分页列表 */
	PageBO<NoticeBO> pageNotices(long current, long size);

	/** 发布公告 */
	void publishNotice(Long id);

	/** 撤回公告 */
	void withdrawNotice(Long id);

	/** 未读公告列表 */
	List<NoticeBO> listUnreadNotices(Long userId);

	/** 标记公告已读 */
	void markNoticeAsRead(Long noticeId, Long userId);
}
