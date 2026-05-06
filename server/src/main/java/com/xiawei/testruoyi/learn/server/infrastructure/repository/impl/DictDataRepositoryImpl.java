package com.xiawei.testruoyi.learn.server.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.basecls.BaseRepositoryImpl;
import com.xiawei.testruoyi.learn.server.domain.user.repository.DictDataRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.mapper.DictDataMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.DictDataPO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 字典数据仓储实现
 */
@Repository
public class DictDataRepositoryImpl extends BaseRepositoryImpl<DictDataMapper, DictDataPO> implements DictDataRepository {

	@Override
	public Page<DictDataPO> findPage(long current, long size, Long dictTypeId) {
		Page<DictDataPO> page = new Page<>(current, size);
		LambdaQueryWrapper<DictDataPO> wrapper = new LambdaQueryWrapper<DictDataPO>()
				.eq(dictTypeId != null, DictDataPO::getDictTypeId, dictTypeId)
				.orderByAsc(DictDataPO::getSort)
				.orderByDesc(DictDataPO::getId);
		return page(page, wrapper);
	}

	@Override
	public List<DictDataPO> listByDictTypeId(Long dictTypeId) {
		return lambdaQuery()
				.eq(DictDataPO::getDictTypeId, dictTypeId)
				.orderByAsc(DictDataPO::getSort)
				.list();
	}

	@Override
	public boolean existsByDictValue(Long dictTypeId, String dictValue, Long excludeId) {
		return lambdaQuery()
				.eq(DictDataPO::getDictTypeId, dictTypeId)
				.eq(DictDataPO::getDictValue, dictValue)
				.ne(excludeId != null, DictDataPO::getId, excludeId)
				.exists();
	}

	@Override
	public void deleteByDictTypeId(Long dictTypeId) {
		LambdaUpdateWrapper<DictDataPO> wrapper = new LambdaUpdateWrapper<DictDataPO>()
				.eq(DictDataPO::getDictTypeId, dictTypeId)
				.set(DictDataPO::getIsDeleted, dictTypeId);
		update(wrapper);
	}
}
