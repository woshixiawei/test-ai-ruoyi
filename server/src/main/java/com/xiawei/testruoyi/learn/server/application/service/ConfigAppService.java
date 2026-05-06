package com.xiawei.testruoyi.learn.server.application.service;

import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.server.application.bo.config.ConfigBO;
import com.xiawei.testruoyi.learn.server.application.bo.config.ConfigCreateBO;

/**
 * 参数配置应用服务接口
 */
public interface ConfigAppService {

	/** 新增参数配置 */
	Long createConfig(ConfigCreateBO bo);

	/** 修改参数配置 */
	void updateConfig(Long id, ConfigCreateBO bo);

	/** 删除参数配置 */
	void deleteConfig(Long id);

	/** 参数配置详情 */
	ConfigBO getConfigById(Long id);

	/** 参数配置分页列表 */
	PageBO<ConfigBO> pageConfigs(long current, long size);

	/** 按参数键名查询配置值 */
	ConfigBO getConfigByKey(String configKey);
}
