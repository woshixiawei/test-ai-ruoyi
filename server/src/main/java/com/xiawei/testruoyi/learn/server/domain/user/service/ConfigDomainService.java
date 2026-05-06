package com.xiawei.testruoyi.learn.server.domain.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.exceptions.BusinessException;
import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.ConfigDO;
import com.xiawei.testruoyi.learn.server.domain.user.repository.ConfigRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.ConfigConvertor;
import com.xiawei.testruoyi.learn.server.infrastructure.po.ConfigPO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 参数配置领域服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigDomainService {

	private final ConfigRepository configRepository;
	private final ConfigConvertor configConvertor;

	/** 保存参数配置 */
	public void save(ConfigDO config) {
		ConfigPO po = configConvertor.toPO(config);
		configRepository.saveOrUpdate(po);
		config.setId(po.getId());
	}

	/** 根据ID查询参数配置 */
	public ConfigDO getById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("参数配置ID不合法");
		}
		ConfigPO po = configRepository.getById(id);
		if (po == null) {
			throw new BusinessException("参数配置不存在");
		}
		return configConvertor.toDomain(po);
	}

	/** 校验参数配置存在 */
	public ConfigDO validateConfigExists(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("参数配置ID不合法");
		}
		ConfigPO po = configRepository.getById(id);
		if (po == null) {
			throw new BusinessException("参数配置不存在");
		}
		return configConvertor.toDomain(po);
	}

	/** 校验参数键名唯一 */
	public void validateConfigKeyNotExists(String configKey, Long excludeId) {
		if (configKey == null || configKey.isBlank()) {
			throw new ParamException("参数键名不能为空");
		}
		if (configRepository.existsByConfigKey(configKey, excludeId)) {
			throw new BusinessException("参数键名已存在");
		}
	}

	/** 删除参数配置 */
	public void deleteById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("参数配置ID不合法");
		}
		configRepository.removeById(id);
	}

	/** 分页查询参数配置 */
	public PageDO<ConfigDO> findPage(long current, long size) {
		Page<ConfigPO> poPage = configRepository.findPage(current, size);
		return PageDO.from(poPage, configConvertor.toDomainList(poPage.getRecords()));
	}

	/** 根据参数键名查询 */
	public ConfigDO getByConfigKey(String configKey) {
		if (configKey == null || configKey.isBlank()) {
			throw new ParamException("参数键名不能为空");
		}
		ConfigPO po = configRepository.getByConfigKey(configKey);
		if (po == null) {
			throw new BusinessException("参数配置不存在");
		}
		return configConvertor.toDomain(po);
	}
}
