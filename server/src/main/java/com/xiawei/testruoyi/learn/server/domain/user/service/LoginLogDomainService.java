package com.xiawei.testruoyi.learn.server.domain.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.infrastructure.mapper.LoginLogMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.LoginLogPO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 登录日志领域服务（只读，无业务规则）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginLogDomainService {

	private final LoginLogMapper loginLogMapper;

	/** 分页查询登录日志 */
	public PageDO<LoginLogPO> findPage(long current, long size) {
		Page<LoginLogPO> page = new Page<>(current, size);
		Page<LoginLogPO> result = loginLogMapper.selectPage(page,
				new LambdaQueryWrapper<LoginLogPO>()
						.orderByDesc(LoginLogPO::getId));
		return PageDO.from(result, result.getRecords());
	}

	/** 清理所有登录日志 */
	public void cleanAll() {
		loginLogMapper.delete(new LambdaQueryWrapper<>());
		log.info("[LoginLogDomainService] 登录日志已全部清理");
	}
}
