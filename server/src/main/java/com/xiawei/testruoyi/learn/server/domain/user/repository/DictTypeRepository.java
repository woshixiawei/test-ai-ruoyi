package com.xiawei.testruoyi.learn.server.domain.user.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.basecls.BaseRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.po.DictTypePO;

/**
 * 字典类型仓储接口
 */
public interface DictTypeRepository extends BaseRepository<DictTypePO> {

	/** 校验字典类型编码是否已存在 */
	boolean existsByDictType(String dictType, Long excludeId);

	/** 分页查询字典类型 */
	Page<DictTypePO> findPage(long current, long size);

	/** 根据字典类型编码查询 */
	DictTypePO getByDictType(String dictType);
}
