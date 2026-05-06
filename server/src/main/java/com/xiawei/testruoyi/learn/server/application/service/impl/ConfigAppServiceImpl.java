package com.xiawei.testruoyi.learn.server.application.service.impl;

import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.application.bo.config.ConfigBO;
import com.xiawei.testruoyi.learn.server.application.bo.config.ConfigCreateBO;
import com.xiawei.testruoyi.learn.server.application.service.ConfigAppService;
import com.xiawei.testruoyi.learn.server.domain.user.do_.ConfigDO;
import com.xiawei.testruoyi.learn.server.domain.user.service.ConfigDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.ConfigConvertor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 参数配置应用服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigAppServiceImpl implements ConfigAppService {

	private final ConfigDomainService configDomainService;
	private final ConfigConvertor configConvertor;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long createConfig(ConfigCreateBO bo) {
		validateCreate(bo);
		configDomainService.validateConfigKeyNotExists(bo.getConfigKey(), null);
		ConfigDO config = configConvertor.toDomainFromCreateBO(bo);
		if (config.getIsSystem() == null) {
			config.setIsSystem(0);
		}
		configDomainService.save(config);
		log.info("[ConfigAppService] 参数配置创建成功, configId={}, configKey={}", config.getId(), bo.getConfigKey());
		return config.getId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateConfig(Long id, ConfigCreateBO bo) {
		validateUpdate(id, bo);
		ConfigDO config = configDomainService.validateConfigExists(id);
		configDomainService.validateConfigKeyNotExists(bo.getConfigKey(), id);
		// 使用MapStruct更新DO（禁止手动setter）
		configConvertor.updateDomainFromBO(bo, config);
		configDomainService.save(config);
		log.info("[ConfigAppService] 参数配置更新成功, configId={}", id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteConfig(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("参数配置ID不合法");
		}
		configDomainService.deleteById(id);
		log.info("[ConfigAppService] 参数配置删除成功, configId={}", id);
	}

	@Override
	public ConfigBO getConfigById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("参数配置ID不合法");
		}
		return configConvertor.toBO(configDomainService.getById(id));
	}

	@Override
	public PageBO<ConfigBO> pageConfigs(long current, long size) {
		PageDO<ConfigDO> doPage = configDomainService.findPage(current, size);
		return PageBO.from(doPage, configConvertor.toBOList(doPage.getRecords()));
	}

	@Override
	public ConfigBO getConfigByKey(String configKey) {
		if (configKey == null || configKey.isBlank()) {
			throw new ParamException("参数键名不能为空");
		}
		return configConvertor.toBO(configDomainService.getByConfigKey(configKey));
	}

	/**
	 * 创建参数配置参数校验（应用层：基本格式校验）
	 */
	private void validateCreate(ConfigCreateBO bo) {
		if (bo == null) {
			throw new ParamException("创建参数不能为空");
		}
		if (bo.getConfigName() == null || bo.getConfigName().isBlank()) {
			throw new ParamException("参数名称不能为空");
		}
		if (bo.getConfigKey() == null || bo.getConfigKey().isBlank()) {
			throw new ParamException("参数键名不能为空");
		}
	}

	/**
	 * 更新参数配置参数校验（应用层：基本格式校验）
	 */
	private void validateUpdate(Long id, ConfigCreateBO bo) {
		if (id == null || id <= 0) {
			throw new ParamException("参数配置ID不合法");
		}
		validateCreate(bo);
	}
}
