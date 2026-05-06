package com.xiawei.testruoyi.learn.server.application.service.impl;

import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.application.bo.dict.DictDataBO;
import com.xiawei.testruoyi.learn.server.application.bo.dict.DictDataCreateBO;
import com.xiawei.testruoyi.learn.server.application.service.DictDataAppService;
import com.xiawei.testruoyi.learn.server.domain.user.do_.DictDataDO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.DictTypeDO;
import com.xiawei.testruoyi.learn.server.domain.user.service.DictDataDomainService;
import com.xiawei.testruoyi.learn.server.domain.user.service.DictTypeDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.DictDataConvertor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典数据应用服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DictDataAppServiceImpl implements DictDataAppService {

	private final DictDataDomainService dictDataDomainService;
	private final DictTypeDomainService dictTypeDomainService;
	private final DictDataConvertor dictDataConvertor;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long createDictData(DictDataCreateBO bo) {
		validateCreate(bo);
		// 校验字典类型存在
		dictTypeDomainService.validateDictTypeExists(bo.getDictTypeId());
		// 校验同一字典类型下数据键值唯一
		dictDataDomainService.validateDictValueNotExists(bo.getDictTypeId(), bo.getDictValue(), null);
		DictDataDO dictData = dictDataConvertor.toDomainFromCreateBO(bo);
		if (dictData.getStatus() == null) {
			dictData.setStatus(1);
		}
		dictDataDomainService.save(dictData);
		log.info("[DictDataAppService] 字典数据创建成功, dictDataId={}, dictTypeId={}", dictData.getId(), bo.getDictTypeId());
		return dictData.getId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateDictData(Long id, DictDataCreateBO bo) {
		validateUpdate(id, bo);
		dictTypeDomainService.validateDictTypeExists(bo.getDictTypeId());
		DictDataDO dictData = dictDataDomainService.validateDictDataExists(id);
		dictDataDomainService.validateDictValueNotExists(bo.getDictTypeId(), bo.getDictValue(), id);
		// 使用MapStruct更新DO（禁止手动setter）
		dictDataConvertor.updateDomainFromBO(bo, dictData);
		dictDataDomainService.save(dictData);
		log.info("[DictDataAppService] 字典数据更新成功, dictDataId={}", id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteDictData(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("字典数据ID不合法");
		}
		dictDataDomainService.deleteById(id);
		log.info("[DictDataAppService] 字典数据删除成功, dictDataId={}", id);
	}

	@Override
	public DictDataBO getDictDataById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("字典数据ID不合法");
		}
		return dictDataConvertor.toBO(dictDataDomainService.getById(id));
	}

	@Override
	public PageBO<DictDataBO> pageDictData(long current, long size, Long dictTypeId) {
		PageDO<DictDataDO> doPage = dictDataDomainService.findPage(current, size, dictTypeId);
		return PageBO.from(doPage, dictDataConvertor.toBOList(doPage.getRecords()));
	}

	@Override
	public List<DictDataBO> listDictDataByType(String dictType) {
		if (dictType == null || dictType.isBlank()) {
			throw new ParamException("字典类型编码不能为空");
		}
		// 根据字典类型编码查找字典类型，再查其下所有字典数据
		DictTypeDO dictTypeDO = dictTypeDomainService.validateDictTypeExistsByCode(dictType);
		List<DictDataDO> doList = dictDataDomainService.listByDictTypeId(dictTypeDO.getId());
		return dictDataConvertor.toBOList(doList);
	}

	/**
	 * 创建字典数据参数校验（应用层：基本格式校验）
	 */
	private void validateCreate(DictDataCreateBO bo) {
		if (bo == null) {
			throw new ParamException("创建参数不能为空");
		}
		if (bo.getDictTypeId() == null || bo.getDictTypeId() <= 0) {
			throw new ParamException("字典类型ID不合法");
		}
		if (bo.getDictLabel() == null || bo.getDictLabel().isBlank()) {
			throw new ParamException("数据标签不能为空");
		}
		if (bo.getDictValue() == null || bo.getDictValue().isBlank()) {
			throw new ParamException("数据键值不能为空");
		}
	}

	/**
	 * 更新字典数据参数校验（应用层：基本格式校验）
	 */
	private void validateUpdate(Long id, DictDataCreateBO bo) {
		if (id == null || id <= 0) {
			throw new ParamException("字典数据ID不合法");
		}
		validateCreate(bo);
	}
}
