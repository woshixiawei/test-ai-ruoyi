package com.xiawei.testruoyi.learn.server.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.basecls.BaseRepositoryImpl;
import com.xiawei.testruoyi.learn.server.domain.user.repository.FileRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.mapper.FileMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.FilePO;
import org.springframework.stereotype.Repository;

/**
 * 文件仓储实现
 */
@Repository
public class FileRepositoryImpl extends BaseRepositoryImpl<FileMapper, FilePO> implements FileRepository {

	@Override
	public Page<FilePO> findPage(long current, long size) {
		Page<FilePO> page = new Page<>(current, size);
		return page(page, new LambdaQueryWrapper<FilePO>()
				.orderByDesc(FilePO::getId));
	}
}
