package com.xiawei.testruoyi.learn.server.application.service.impl;

import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.application.bo.log.OperLogBO;
import com.xiawei.testruoyi.learn.server.application.service.OperLogAppService;
import com.xiawei.testruoyi.learn.server.domain.user.service.OperLogDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.OperLogConvertor;
import com.xiawei.testruoyi.learn.server.infrastructure.po.OperLogPO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 操作日志应用服务实现
 *
 * <p>只通过 OperLogDomainService 访问数据层，不直接操作Mapper。</p>
 * <p>Convertor调用集中在AppService层，Controller不直接操作Convertor。</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OperLogAppServiceImpl implements OperLogAppService {

	private final OperLogDomainService operLogDomainService;
	private final OperLogConvertor operLogConvertor;

	@Override
	public PageBO<OperLogBO> pageOperLogs(long current, long size) {
		PageDO<OperLogPO> poPage = operLogDomainService.findPage(current, size);
		return PageBO.from(poPage, operLogConvertor.toBOList(poPage.getRecords()));
	}

	@Override
	public void cleanAll() {
		operLogDomainService.cleanAll();
		log.info("[OperLogAppService] 操作日志已全部清理");
	}
}
