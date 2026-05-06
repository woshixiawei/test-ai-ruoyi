package com.xiawei.testruoyi.learn.server.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.basecls.BaseRepositoryImpl;
import com.xiawei.testruoyi.learn.server.domain.user.repository.DictTypeRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.mapper.DictTypeMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.DictTypePO;
import org.springframework.stereotype.Repository;

/**
 * 字典类型仓储实现
 */
@Repository
public class DictTypeRepositoryImpl extends BaseRepositoryImpl<DictTypeMapper, DictTypePO> implements DictTypeRepository {

	@Override
	public boolean existsByDictType(String dictType, Long excludeId) {
		return lambdaQuery()
				.eq(DictTypePO::getDictType, dictType)
				.ne(excludeId != null, DictTypePO::getId, excludeId)
				.exists();
	}

	@Override
	public Page<DictTypePO> findPage(long current, long size) {
		Page<DictTypePO> page = new Page<>(current, size);
		return page(page, new LambdaQueryWrapper<DictTypePO>()
				.orderByDesc(DictTypePO::getId));
	}

	@Override
	public DictTypePO getByDictType(String dictType) {
		return lambdaQuery()
				.eq(DictTypePO::getDictType, dictType)
				.one();
	}
}
