package com.xiawei.testruoyi.learn.server.domain.user.service;

import com.xiawei.testruoyi.learn.base.exceptions.BusinessException;
import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.DictDataDO;
import com.xiawei.testruoyi.learn.server.domain.user.repository.DictDataRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.DictDataConvertor;
import com.xiawei.testruoyi.learn.server.infrastructure.po.DictDataPO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典数据领域服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DictDataDomainService {

	private final DictDataRepository dictDataRepository;
	private final DictDataConvertor dictDataConvertor;

	/** 保存字典数据 */
	public void save(DictDataDO dictData) {
		DictDataPO po = dictDataConvertor.toPO(dictData);
		dictDataRepository.saveOrUpdate(po);
		dictData.setId(po.getId());
	}

	/** 根据ID查询字典数据 */
	public DictDataDO getById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("字典数据ID不合法");
		}
		DictDataPO po = dictDataRepository.getById(id);
		if (po == null) {
			throw new BusinessException("字典数据不存在");
		}
		return dictDataConvertor.toDomain(po);
	}

	/** 校验字典数据存在 */
	public DictDataDO validateDictDataExists(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("字典数据ID不合法");
		}
		DictDataPO po = dictDataRepository.getById(id);
		if (po == null) {
			throw new BusinessException("字典数据不存在");
		}
		return dictDataConvertor.toDomain(po);
	}

	/** 校验同一字典类型下数据键值唯一 */
	public void validateDictValueNotExists(Long dictTypeId, String dictValue, Long excludeId) {
		if (dictTypeId == null || dictTypeId <= 0) {
			throw new ParamException("字典类型ID不合法");
		}
		if (dictValue == null || dictValue.isBlank()) {
			throw new ParamException("数据键值不能为空");
		}
		if (dictDataRepository.existsByDictValue(dictTypeId, dictValue, excludeId)) {
			throw new BusinessException("同一字典类型下数据键值已存在");
		}
	}

	/** 删除字典数据 */
	public void deleteById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("字典数据ID不合法");
		}
		dictDataRepository.removeById(id);
	}

	/** 逻辑删除指定字典类型下的所有字典数据（级联删除） */
	public void deleteByDictTypeId(Long dictTypeId) {
		if (dictTypeId == null || dictTypeId <= 0) {
			throw new ParamException("字典类型ID不合法");
		}
		dictDataRepository.deleteByDictTypeId(dictTypeId);
	}

	/** 分页查询字典数据 */
	public PageDO<DictDataDO> findPage(long current, long size, Long dictTypeId) {
		Page<DictDataPO> poPage = dictDataRepository.findPage(current, size, dictTypeId);
		return PageDO.from(poPage, dictDataConvertor.toDomainList(poPage.getRecords()));
	}

	/** 查询指定字典类型下的所有字典数据 */
	public List<DictDataDO> listByDictTypeId(Long dictTypeId) {
		if (dictTypeId == null || dictTypeId <= 0) {
			throw new ParamException("字典类型ID不合法");
		}
		List<DictDataPO> poList = dictDataRepository.listByDictTypeId(dictTypeId);
		return dictDataConvertor.toDomainList(poList);
	}
}
