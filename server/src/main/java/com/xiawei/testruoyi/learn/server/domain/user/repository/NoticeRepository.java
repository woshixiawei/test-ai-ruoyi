package com.xiawei.testruoyi.learn.server.domain.user.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.basecls.BaseRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.po.NoticePO;

import java.util.List;

/**
 * 通知公告仓储接口
 */
public interface NoticeRepository extends BaseRepository<NoticePO> {

	/** 分页查询通知公告 */
	Page<NoticePO> findPage(long current, long size);

	/** 查询用户未读公告列表 */
	List<NoticePO> listUnreadByUserId(Long userId);

	/** 标记公告已读 */
	void markAsRead(Long noticeId, Long userId);

	/** 校验用户是否已读公告 */
	boolean existsReadRecord(Long noticeId, Long userId);
}
