package com.xiawei.testruoyi.learn.server.domain.user.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.basecls.BaseRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.po.ConfigPO;

/**
 * 参数配置仓储接口
 */
public interface ConfigRepository extends BaseRepository<ConfigPO> {

	/** 校验参数键名是否已存在 */
	boolean existsByConfigKey(String configKey, Long excludeId);

	/** 分页查询参数配置 */
	Page<ConfigPO> findPage(long current, long size);

	/** 根据参数键名查询 */
	ConfigPO getByConfigKey(String configKey);
}
