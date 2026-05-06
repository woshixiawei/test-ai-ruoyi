package com.xiawei.testruoyi.learn.server.application.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.xiawei.testruoyi.learn.base.exceptions.BusinessException;
import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.application.bo.log.LoginLogBO;
import com.xiawei.testruoyi.learn.server.application.bo.notice.NoticeBO;
import com.xiawei.testruoyi.learn.server.application.bo.user.ProfileBO;
import com.xiawei.testruoyi.learn.server.application.bo.user.UserCreateBO;
import com.xiawei.testruoyi.learn.server.application.service.NoticeAppService;
import com.xiawei.testruoyi.learn.server.application.service.ProfileAppService;
import com.xiawei.testruoyi.learn.server.domain.user.do_.UserDO;
import com.xiawei.testruoyi.learn.server.domain.user.service.LoginLogDomainService;
import com.xiawei.testruoyi.learn.server.domain.user.service.UserDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.LoginLogConvertor;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.UserConvertor;
import com.xiawei.testruoyi.learn.server.infrastructure.po.LoginLogPO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 个人中心应用服务实现
 *
 * <p>跨域调用通过UserDomainService和LoginLogDomainService。</p>
 * <p>Convertor调用集中在AppService层，Controller不直接操作Convertor。</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileAppServiceImpl implements ProfileAppService {

	private final UserDomainService userDomainService;
	private final UserConvertor userConvertor;
	private final LoginLogDomainService loginLogDomainService;
	private final LoginLogConvertor loginLogConvertor;
	private final NoticeAppService noticeAppService;

	@Override
	public ProfileBO getProfile() {
		Long userId = StpUtil.getLoginIdAsLong();
		UserDO user = userDomainService.getById(userId);
		ProfileBO profile = userConvertor.toProfileBO(user);
		return profile;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateProfile(UserCreateBO bo) {
		Long userId = StpUtil.getLoginIdAsLong();
		UserDO user = userDomainService.validateUserExists(userId);
		userConvertor.updateDomainFromBO(bo, user);
		userDomainService.save(user);
		log.info("[ProfileAppService] 个人信息修改成功, userId={}", userId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void changePassword(String oldPassword, String newPassword) {
		// 1. 参数校验
		if (oldPassword == null || oldPassword.isBlank()) {
			throw new ParamException("旧密码不能为空");
		}
		if (newPassword == null || newPassword.isBlank()) {
			throw new ParamException("新密码不能为空");
		}
		if (newPassword.length() < 6) {
			throw new ParamException("新密码长度不能少于6个字符");
		}

		// 2. 验证旧密码
		Long userId = StpUtil.getLoginIdAsLong();
		UserDO user = userDomainService.getById(userId);
		if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
			throw new BusinessException("旧密码不正确");
		}

		// 3. 设置新密码
		user.setPassword(BCrypt.hashpw(newPassword));
		userDomainService.save(user);
		log.info("[ProfileAppService] 密码修改成功, userId={}", userId);
	}

	@Override
	public PageBO<LoginLogBO> pageLoginLogs(long current, long size) {
		PageDO<LoginLogPO> poPage = loginLogDomainService.findPage(current, size);
		return PageBO.from(poPage, loginLogConvertor.toBOList(poPage.getRecords()));
	}

	@Override
	public List<NoticeBO> listMessages() {
		Long userId = StpUtil.getLoginIdAsLong();
		return noticeAppService.listUnreadNotices(userId);
	}
}
