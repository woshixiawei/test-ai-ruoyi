package com.xiawei.testruoyi.learn.server.application.service.impl;

import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.application.bo.log.LoginLogBO;
import com.xiawei.testruoyi.learn.server.application.service.LoginLogAppService;
import com.xiawei.testruoyi.learn.server.domain.user.service.LoginLogDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.LoginLogConvertor;
import com.xiawei.testruoyi.learn.server.infrastructure.po.LoginLogPO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 登录日志应用服务实现
 *
 * <p>只通过 LoginLogDomainService 访问数据层，不直接操作Mapper。</p>
 * <p>Convertor调用集中在AppService层，Controller不直接操作Convertor。</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginLogAppServiceImpl implements LoginLogAppService {

	private final LoginLogDomainService loginLogDomainService;
	private final LoginLogConvertor loginLogConvertor;

	@Override
	public PageBO<LoginLogBO> pageLoginLogs(long current, long size) {
		PageDO<LoginLogPO> poPage = loginLogDomainService.findPage(current, size);
		return PageBO.from(poPage, loginLogConvertor.toBOList(poPage.getRecords()));
	}

	@Override
	public void cleanAll() {
		loginLogDomainService.cleanAll();
		log.info("[LoginLogAppService] 登录日志已全部清理");
	}
}
