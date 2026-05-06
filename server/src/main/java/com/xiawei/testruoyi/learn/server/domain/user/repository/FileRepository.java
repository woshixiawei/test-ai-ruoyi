package com.xiawei.testruoyi.learn.server.domain.user.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.basecls.BaseRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.po.FilePO;

/**
 * 文件仓储接口
 */
public interface FileRepository extends BaseRepository<FilePO> {

	/** 分页查询文件 */
	Page<FilePO> findPage(long current, long size);
}
