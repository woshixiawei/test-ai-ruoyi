package com.xiawei.testruoyi.learn.server.application.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.application.bo.notice.NoticeBO;
import com.xiawei.testruoyi.learn.server.application.bo.notice.NoticeCreateBO;
import com.xiawei.testruoyi.learn.server.application.service.NoticeAppService;
import com.xiawei.testruoyi.learn.server.domain.user.do_.NoticeDO;
import com.xiawei.testruoyi.learn.server.domain.user.service.NoticeDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.NoticeConvertor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 通知公告应用服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeAppServiceImpl implements NoticeAppService {

	private final NoticeDomainService noticeDomainService;
	private final NoticeConvertor noticeConvertor;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long createNotice(NoticeCreateBO bo) {
		validateCreate(bo);
		NoticeDO notice = noticeConvertor.toDomainFromCreateBO(bo);
		if (notice.getStatus() == null) {
			notice.setStatus(0); // 默认草稿
		}
		noticeDomainService.save(notice);
		log.info("[NoticeAppService] 公告创建成功, noticeId={}", notice.getId());
		return notice.getId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateNotice(Long id, NoticeCreateBO bo) {
		validateUpdate(id, bo);
		NoticeDO notice = noticeDomainService.validateNoticeExists(id);
		// 使用MapStruct更新DO（禁止手动setter）
		noticeConvertor.updateDomainFromBO(bo, notice);
		noticeDomainService.save(notice);
		log.info("[NoticeAppService] 公告更新成功, noticeId={}", id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteNotice(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("公告ID不合法");
		}
		noticeDomainService.deleteById(id);
		log.info("[NoticeAppService] 公告删除成功, noticeId={}", id);
	}

	@Override
	public NoticeBO getNoticeById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("公告ID不合法");
		}
		return noticeConvertor.toBO(noticeDomainService.getById(id));
	}

	@Override
	public PageBO<NoticeBO> pageNotices(long current, long size) {
		PageDO<NoticeDO> doPage = noticeDomainService.findPage(current, size);
		return PageBO.from(doPage, noticeConvertor.toBOList(doPage.getRecords()));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void publishNotice(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("公告ID不合法");
		}
		NoticeDO notice = noticeDomainService.validateNoticeExists(id);
		Long publisherId = StpUtil.getLoginIdAsLong();
		noticeDomainService.publish(notice, publisherId);
		log.info("[NoticeAppService] 公告发布成功, noticeId={}", id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void withdrawNotice(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("公告ID不合法");
		}
		NoticeDO notice = noticeDomainService.validateNoticeExists(id);
		noticeDomainService.withdraw(notice);
		log.info("[NoticeAppService] 公告撤回成功, noticeId={}", id);
	}

	@Override
	public List<NoticeBO> listUnreadNotices(Long userId) {
		if (userId == null || userId <= 0) {
			throw new ParamException("用户ID不合法");
		}
		return noticeConvertor.toBOList(noticeDomainService.listUnreadByUserId(userId));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void markNoticeAsRead(Long noticeId, Long userId) {
		if (noticeId == null || noticeId <= 0) {
			throw new ParamException("公告ID不合法");
		}
		if (userId == null || userId <= 0) {
			throw new ParamException("用户ID不合法");
		}
		noticeDomainService.markAsRead(noticeId, userId);
		log.info("[NoticeAppService] 公告标记已读, noticeId={}, userId={}", noticeId, userId);
	}

	/**
	 * 创建公告参数校验（应用层：基本格式校验）
	 */
	private void validateCreate(NoticeCreateBO bo) {
		if (bo == null) {
			throw new ParamException("创建参数不能为空");
		}
		if (bo.getTitle() == null || bo.getTitle().isBlank()) {
			throw new ParamException("公告标题不能为空");
		}
	}

	/**
	 * 更新公告参数校验（应用层：基本格式校验）
	 */
	private void validateUpdate(Long id, NoticeCreateBO bo) {
		if (id == null || id <= 0) {
			throw new ParamException("公告ID不合法");
		}
		validateCreate(bo);
	}
}
