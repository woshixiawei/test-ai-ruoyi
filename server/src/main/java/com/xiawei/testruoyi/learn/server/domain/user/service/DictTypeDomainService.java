package com.xiawei.testruoyi.learn.server.domain.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.exceptions.BusinessException;
import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.DictTypeDO;
import com.xiawei.testruoyi.learn.server.domain.user.repository.DictTypeRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.DictTypeConvertor;
import com.xiawei.testruoyi.learn.server.infrastructure.po.DictTypePO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 字典类型领域服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DictTypeDomainService {

	private final DictTypeRepository dictTypeRepository;
	private final DictTypeConvertor dictTypeConvertor;

	/** 保存字典类型 */
	public void save(DictTypeDO dictType) {
		DictTypePO po = dictTypeConvertor.toPO(dictType);
		dictTypeRepository.saveOrUpdate(po);
		dictType.setId(po.getId());
	}

	/** 根据ID查询字典类型 */
	public DictTypeDO getById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("字典类型ID不合法");
		}
		DictTypePO po = dictTypeRepository.getById(id);
		if (po == null) {
			throw new BusinessException("字典类型不存在");
		}
		return dictTypeConvertor.toDomain(po);
	}

	/** 校验字典类型存在 */
	public DictTypeDO validateDictTypeExists(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("字典类型ID不合法");
		}
		DictTypePO po = dictTypeRepository.getById(id);
		if (po == null) {
			throw new BusinessException("字典类型不存在");
		}
		return dictTypeConvertor.toDomain(po);
	}

	/** 校验字典类型编码唯一 */
	public void validateDictTypeNotExists(String dictType, Long excludeId) {
		if (dictType == null || dictType.isBlank()) {
			throw new ParamException("字典类型编码不能为空");
		}
		if (dictTypeRepository.existsByDictType(dictType, excludeId)) {
			throw new BusinessException("字典类型编码已存在");
		}
	}

	/** 删除字典类型 */
	public void deleteById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("字典类型ID不合法");
		}
		dictTypeRepository.removeById(id);
	}

	/** 分页查询字典类型 */
	public PageDO<DictTypeDO> findPage(long current, long size) {
		Page<DictTypePO> poPage = dictTypeRepository.findPage(current, size);
		return PageDO.from(poPage, dictTypeConvertor.toDomainList(poPage.getRecords()));
	}

	/** 根据字典类型编码校验并获取字典类型 */
	public DictTypeDO validateDictTypeExistsByCode(String dictType) {
		if (dictType == null || dictType.isBlank()) {
			throw new ParamException("字典类型编码不能为空");
		}
		DictTypePO po = dictTypeRepository.getByDictType(dictType);
		if (po == null) {
			throw new BusinessException("字典类型不存在");
		}
		return dictTypeConvertor.toDomain(po);
	}
}
