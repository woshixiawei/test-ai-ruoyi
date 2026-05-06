package com.xiawei.testruoyi.learn.server.domain.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.exceptions.BusinessException;
import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.NoticeDO;
import com.xiawei.testruoyi.learn.server.domain.user.repository.NoticeRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.NoticeConvertor;
import com.xiawei.testruoyi.learn.server.infrastructure.po.NoticePO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 通知公告领域服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeDomainService {

	private final NoticeRepository noticeRepository;
	private final NoticeConvertor noticeConvertor;

	/** 保存通知公告 */
	public void save(NoticeDO notice) {
		NoticePO po = noticeConvertor.toPO(notice);
		noticeRepository.saveOrUpdate(po);
		notice.setId(po.getId());
	}

	/** 根据ID查询通知公告 */
	public NoticeDO getById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("公告ID不合法");
		}
		NoticePO po = noticeRepository.getById(id);
		if (po == null) {
			throw new BusinessException("公告不存在");
		}
		return noticeConvertor.toDomain(po);
	}

	/** 校验公告存在 */
	public NoticeDO validateNoticeExists(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("公告ID不合法");
		}
		NoticePO po = noticeRepository.getById(id);
		if (po == null) {
			throw new BusinessException("公告不存在");
		}
		return noticeConvertor.toDomain(po);
	}

	/** 删除通知公告 */
	public void deleteById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("公告ID不合法");
		}
		noticeRepository.removeById(id);
	}

	/** 分页查询通知公告 */
	public PageDO<NoticeDO> findPage(long current, long size) {
		Page<NoticePO> poPage = noticeRepository.findPage(current, size);
		return PageDO.from(poPage, noticeConvertor.toDomainList(poPage.getRecords()));
	}

	/** 发布公告（状态改为已发布，设置发布时间和发布人） */
	public void publish(NoticeDO notice, Long publisherId) {
		notice.setStatus(1);
		notice.setPublisherId(publisherId);
		notice.setPublishTime(new Date());
		save(notice);
	}

	/** 撤回公告（状态改为已撤回） */
	public void withdraw(NoticeDO notice) {
		notice.setStatus(2);
		save(notice);
	}

	/** 标记公告已读 */
	public void markAsRead(Long noticeId, Long userId) {
		if (noticeId == null || noticeId <= 0) {
			throw new ParamException("公告ID不合法");
		}
		if (userId == null || userId <= 0) {
			throw new ParamException("用户ID不合法");
		}
		noticeRepository.markAsRead(noticeId, userId);
	}

	/** 查询用户未读公告列表 */
	public List<NoticeDO> listUnreadByUserId(Long userId) {
		if (userId == null || userId <= 0) {
			throw new ParamException("用户ID不合法");
		}
		List<NoticePO> poList = noticeRepository.listUnreadByUserId(userId);
		return noticeConvertor.toDomainList(poList);
	}
}
