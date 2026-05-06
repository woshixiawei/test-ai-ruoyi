package com.xiawei.testruoyi.learn.server.application.service;

import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.server.application.bo.dict.DictTypeBO;
import com.xiawei.testruoyi.learn.server.application.bo.dict.DictTypeCreateBO;

/**
 * 字典类型应用服务接口
 */
public interface DictTypeAppService {

	/** 新增字典类型 */
	Long createDictType(DictTypeCreateBO bo);

	/** 修改字典类型 */
	void updateDictType(Long id, DictTypeCreateBO bo);

	/** 删除字典类型 */
	void deleteDictType(Long id);

	/** 字典类型详情 */
	DictTypeBO getDictTypeById(Long id);

	/** 字典类型分页列表 */
	PageBO<DictTypeBO> pageDictTypes(long current, long size);
}
