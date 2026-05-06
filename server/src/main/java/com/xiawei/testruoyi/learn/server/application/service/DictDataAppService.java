package com.xiawei.testruoyi.learn.server.application.service;

import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.server.application.bo.dict.DictDataBO;
import com.xiawei.testruoyi.learn.server.application.bo.dict.DictDataCreateBO;

import java.util.List;

/**
 * 字典数据应用服务接口
 */
public interface DictDataAppService {

	/** 新增字典数据 */
	Long createDictData(DictDataCreateBO bo);

	/** 修改字典数据 */
	void updateDictData(Long id, DictDataCreateBO bo);

	/** 删除字典数据 */
	void deleteDictData(Long id);

	/** 字典数据详情 */
	DictDataBO getDictDataById(Long id);

	/** 字典数据分页列表 */
	PageBO<DictDataBO> pageDictData(long current, long size, Long dictTypeId);

	/** 按字典类型编码查询字典数据列表 */
	List<DictDataBO> listDictDataByType(String dictType);
}
