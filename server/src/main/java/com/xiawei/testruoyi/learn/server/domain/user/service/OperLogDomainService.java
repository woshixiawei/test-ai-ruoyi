package com.xiawei.testruoyi.learn.server.domain.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.infrastructure.mapper.OperLogMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.OperLogPO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 操作日志领域服务（只读，无业务规则）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OperLogDomainService {

	private final OperLogMapper operLogMapper;

	/** 分页查询操作日志 */
	public PageDO<OperLogPO> findPage(long current, long size) {
		Page<OperLogPO> page = new Page<>(current, size);
		Page<OperLogPO> result = operLogMapper.selectPage(page,
				new LambdaQueryWrapper<OperLogPO>()
						.orderByDesc(OperLogPO::getId));
		return PageDO.from(result, result.getRecords());
	}

	/** 清理所有操作日志 */
	public void cleanAll() {
		operLogMapper.delete(new LambdaQueryWrapper<>());
		log.info("[OperLogDomainService] 操作日志已全部清理");
	}
}
