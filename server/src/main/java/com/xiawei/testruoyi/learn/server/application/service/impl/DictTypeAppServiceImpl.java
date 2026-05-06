package com.xiawei.testruoyi.learn.server.application.service.impl;

import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.application.bo.dict.DictTypeBO;
import com.xiawei.testruoyi.learn.server.application.bo.dict.DictTypeCreateBO;
import com.xiawei.testruoyi.learn.server.application.service.DictTypeAppService;
import com.xiawei.testruoyi.learn.server.domain.user.do_.DictTypeDO;
import com.xiawei.testruoyi.learn.server.domain.user.service.DictTypeDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.DictTypeConvertor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字典类型应用服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DictTypeAppServiceImpl implements DictTypeAppService {

	private final DictTypeDomainService dictTypeDomainService;
	private final DictTypeConvertor dictTypeConvertor;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long createDictType(DictTypeCreateBO bo) {
		validateCreate(bo);
		dictTypeDomainService.validateDictTypeNotExists(bo.getDictType(), null);
		DictTypeDO dictType = dictTypeConvertor.toDomainFromCreateBO(bo);
		if (dictType.getStatus() == null) {
			dictType.setStatus(1);
		}
		dictTypeDomainService.save(dictType);
		log.info("[DictTypeAppService] 字典类型创建成功, dictTypeId={}, dictType={}", dictType.getId(), bo.getDictType());
		return dictType.getId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateDictType(Long id, DictTypeCreateBO bo) {
		validateUpdate(id, bo);
		DictTypeDO dictType = dictTypeDomainService.validateDictTypeExists(id);
		dictTypeDomainService.validateDictTypeNotExists(bo.getDictType(), id);
		// 使用MapStruct更新DO（禁止手动setter）
		dictTypeConvertor.updateDomainFromBO(bo, dictType);
		dictTypeDomainService.save(dictType);
		log.info("[DictTypeAppService] 字典类型更新成功, dictTypeId={}", id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteDictType(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("字典类型ID不合法");
		}
		dictTypeDomainService.deleteById(id);
		log.info("[DictTypeAppService] 字典类型删除成功, dictTypeId={}", id);
	}

	@Override
	public DictTypeBO getDictTypeById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("字典类型ID不合法");
		}
		return dictTypeConvertor.toBO(dictTypeDomainService.getById(id));
	}

	@Override
	public PageBO<DictTypeBO> pageDictTypes(long current, long size) {
		PageDO<DictTypeDO> doPage = dictTypeDomainService.findPage(current, size);
		return PageBO.from(doPage, dictTypeConvertor.toBOList(doPage.getRecords()));
	}

	/**
	 * 创建字典类型参数校验（应用层：基本格式校验）
	 */
	private void validateCreate(DictTypeCreateBO bo) {
		if (bo == null) {
			throw new ParamException("创建参数不能为空");
		}
		if (bo.getDictName() == null || bo.getDictName().isBlank()) {
			throw new ParamException("字典名称不能为空");
		}
		if (bo.getDictType() == null || bo.getDictType().isBlank()) {
			throw new ParamException("字典类型编码不能为空");
		}
	}

	/**
	 * 更新字典类型参数校验（应用层：基本格式校验）
	 */
	private void validateUpdate(Long id, DictTypeCreateBO bo) {
		if (id == null || id <= 0) {
			throw new ParamException("字典类型ID不合法");
		}
		validateCreate(bo);
	}
}
