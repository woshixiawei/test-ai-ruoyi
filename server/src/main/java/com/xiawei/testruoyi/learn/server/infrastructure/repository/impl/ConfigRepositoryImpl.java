package com.xiawei.testruoyi.learn.server.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.basecls.BaseRepositoryImpl;
import com.xiawei.testruoyi.learn.server.domain.user.repository.ConfigRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.mapper.ConfigMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.ConfigPO;
import org.springframework.stereotype.Repository;

/**
 * 参数配置仓储实现
 */
@Repository
public class ConfigRepositoryImpl extends BaseRepositoryImpl<ConfigMapper, ConfigPO> implements ConfigRepository {

	@Override
	public boolean existsByConfigKey(String configKey, Long excludeId) {
		return lambdaQuery()
				.eq(ConfigPO::getConfigKey, configKey)
				.ne(excludeId != null, ConfigPO::getId, excludeId)
				.exists();
	}

	@Override
	public Page<ConfigPO> findPage(long current, long size) {
		Page<ConfigPO> page = new Page<>(current, size);
		return page(page, new LambdaQueryWrapper<ConfigPO>()
				.orderByDesc(ConfigPO::getId));
	}

	@Override
	public ConfigPO getByConfigKey(String configKey) {
		return lambdaQuery()
				.eq(ConfigPO::getConfigKey, configKey)
				.one();
	}
}
