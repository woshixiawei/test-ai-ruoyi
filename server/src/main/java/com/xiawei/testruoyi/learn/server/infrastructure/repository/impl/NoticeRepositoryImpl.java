package com.xiawei.testruoyi.learn.server.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.basecls.BaseRepositoryImpl;
import com.xiawei.testruoyi.learn.server.domain.user.repository.NoticeRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.mapper.NoticeMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.mapper.NoticeReadMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.NoticePO;
import com.xiawei.testruoyi.learn.server.infrastructure.po.NoticeReadPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 通知公告仓储实现
 */
@Repository
@RequiredArgsConstructor
public class NoticeRepositoryImpl extends BaseRepositoryImpl<NoticeMapper, NoticePO> implements NoticeRepository {

	private final NoticeReadMapper noticeReadMapper;

	@Override
	public Page<NoticePO> findPage(long current, long size) {
		Page<NoticePO> page = new Page<>(current, size);
		return page(page, new LambdaQueryWrapper<NoticePO>()
				.orderByDesc(NoticePO::getId));
	}

	@Override
	public List<NoticePO> listUnreadByUserId(Long userId) {
		// 查询已发布状态的公告ID列表中，排除已读的
		List<Long> readNoticeIds = noticeReadMapper.selectList(
				new LambdaQueryWrapper<NoticeReadPO>()
						.eq(NoticeReadPO::getUserId, userId)
						.select(NoticeReadPO::getNoticeId)
		).stream().map(NoticeReadPO::getNoticeId).toList();

		LambdaQueryWrapper<NoticePO> wrapper = new LambdaQueryWrapper<NoticePO>()
				.eq(NoticePO::getStatus, 1)
				.notIn(!readNoticeIds.isEmpty(), NoticePO::getId, readNoticeIds)
				.orderByDesc(NoticePO::getPublishTime);
		return list(wrapper);
	}

	@Override
	public void markAsRead(Long noticeId, Long userId) {
		// 检查是否已读，避免重复插入
		Long count = noticeReadMapper.selectCount(
				new LambdaQueryWrapper<NoticeReadPO>()
						.eq(NoticeReadPO::getNoticeId, noticeId)
						.eq(NoticeReadPO::getUserId, userId));
		if (count == 0) {
			NoticeReadPO readPO = new NoticeReadPO();
			readPO.setNoticeId(noticeId);
			readPO.setUserId(userId);
			readPO.setReadTime(new Date());
			readPO.setCreateTime(new Date());
			readPO.setUpdateTime(new Date());
			noticeReadMapper.insert(readPO);
		}
	}

	@Override
	public boolean existsReadRecord(Long noticeId, Long userId) {
		return noticeReadMapper.selectCount(
				new LambdaQueryWrapper<NoticeReadPO>()
						.eq(NoticeReadPO::getNoticeId, noticeId)
						.eq(NoticeReadPO::getUserId, userId)) > 0;
	}
}
