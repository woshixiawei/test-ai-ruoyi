package com.xiawei.testruoyi.learn.server.application.service;

import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.server.application.bo.post.PostBO;
import com.xiawei.testruoyi.learn.server.application.bo.post.PostCreateBO;

/**
 * 岗位应用服务接口
 */
public interface PostAppService {

	/** 新增岗位 */
	Long createPost(PostCreateBO bo);

	/** 修改岗位 */
	void updatePost(Long id, PostCreateBO bo);

	/** 删除岗位 */
	void deletePost(Long id);

	/** 岗位详情 */
	PostBO getPostById(Long id);

	/** 岗位分页列表 */
	PageBO<PostBO> pagePosts(long current, long size);
}
