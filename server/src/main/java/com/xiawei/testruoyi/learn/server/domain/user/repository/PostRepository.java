package com.xiawei.testruoyi.learn.server.domain.user.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.basecls.BaseRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.po.PostPO;

/**
 * 岗位仓储接口
 */
public interface PostRepository extends BaseRepository<PostPO> {

	/**
	 * 校验岗位编码是否已存在
	 */
	boolean existsByPostCode(String postCode, Long excludeId);

	/**
	 * 分页查询岗位
	 */
	Page<PostPO> findPage(long current, long size);
}
