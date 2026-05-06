package com.xiawei.testruoyi.learn.server.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.basecls.BaseRepositoryImpl;
import com.xiawei.testruoyi.learn.server.domain.user.repository.PostRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.mapper.PostMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.PostPO;
import org.springframework.stereotype.Repository;

/**
 * 岗位仓储实现
 */
@Repository
public class PostRepositoryImpl extends BaseRepositoryImpl<PostMapper, PostPO> implements PostRepository {

	@Override
	public boolean existsByPostCode(String postCode, Long excludeId) {
		return lambdaQuery()
				.eq(PostPO::getPostCode, postCode)
				.ne(excludeId != null, PostPO::getId, excludeId)
				.exists();
	}

	@Override
	public Page<PostPO> findPage(long current, long size) {
		Page<PostPO> page = new Page<>(current, size);
		return page(page, new LambdaQueryWrapper<PostPO>().orderByAsc(PostPO::getSort));
	}
}
