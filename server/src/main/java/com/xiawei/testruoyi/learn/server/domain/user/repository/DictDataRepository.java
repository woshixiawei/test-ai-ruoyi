package com.xiawei.testruoyi.learn.server.domain.user.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.basecls.BaseRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.po.DictDataPO;

import java.util.List;

/**
 * 字典数据仓储接口
 */
public interface DictDataRepository extends BaseRepository<DictDataPO> {

	/** 分页查询字典数据 */
	Page<DictDataPO> findPage(long current, long size, Long dictTypeId);

	/** 查询指定字典类型下的所有字典数据 */
	List<DictDataPO> listByDictTypeId(Long dictTypeId);

	/** 校验同一字典类型下数据键值是否已存在 */
	boolean existsByDictValue(Long dictTypeId, String dictValue, Long excludeId);

	/** 逻辑删除指定字典类型下的所有字典数据 */
	void deleteByDictTypeId(Long dictTypeId);
}
