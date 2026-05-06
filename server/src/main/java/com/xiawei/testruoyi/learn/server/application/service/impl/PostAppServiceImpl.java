package com.xiawei.testruoyi.learn.server.application.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.application.service.PostAppService;
import com.xiawei.testruoyi.learn.server.application.bo.post.PostBO;
import com.xiawei.testruoyi.learn.server.application.bo.post.PostCreateBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.PostDO;
import com.xiawei.testruoyi.learn.server.domain.user.service.PostDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.PostConvertor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 岗位应用服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostAppServiceImpl implements PostAppService {

	private final PostDomainService postDomainService;
	private final PostConvertor postConvertor;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long createPost(PostCreateBO bo) {
		validateCreate(bo);
		postDomainService.validatePostCodeNotExists(bo.getPostCode(), null);
		PostDO post = postConvertor.toDomainFromCreateBO(bo);
		if (post.getStatus() == null) {
			post.setStatus(1);
		}
		postDomainService.save(post);
		log.info("[PostAppService] 岗位创建成功, postId={}", post.getId());
		return post.getId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updatePost(Long id, PostCreateBO bo) {
		validateUpdate(id, bo);
		PostDO post = postDomainService.validatePostExists(id);
		postDomainService.validatePostCodeNotExists(bo.getPostCode(), id);
		// 使用MapStruct更新DO（禁止手动setter）
		postConvertor.updateDomainFromBO(bo, post);
		postDomainService.save(post);
		log.info("[PostAppService] 岗位更新成功, postId={}", id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deletePost(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("岗位ID不合法");
		}
		postDomainService.validatePostExists(id);
		postDomainService.deleteById(id);
		log.info("[PostAppService] 岗位删除成功, postId={}", id);
	}

	@Override
	public PostBO getPostById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("岗位ID不合法");
		}
		return postConvertor.toBO(postDomainService.getById(id));
	}

	@Override
	public PageBO<PostBO> pagePosts(long current, long size) {
		PageDO<PostDO> doPage = postDomainService.findPage(current, size);
		return PageBO.from(doPage, postConvertor.toBOList(doPage.getRecords()));
	}

	/**
	 * 创建岗位参数校验（应用层：基本格式校验）
	 */
	private void validateCreate(PostCreateBO bo) {
		if (bo == null) {
			throw new ParamException("创建参数不能为空");
		}
		if (bo.getPostCode() == null || bo.getPostCode().isBlank()) {
			throw new ParamException("岗位编码不能为空");
		}
		if (bo.getPostName() == null || bo.getPostName().isBlank()) {
			throw new ParamException("岗位名称不能为空");
		}
	}

	/**
	 * 更新岗位参数校验（应用层：基本格式校验）
	 */
	private void validateUpdate(Long id, PostCreateBO bo) {
		if (id == null || id <= 0) {
			throw new ParamException("岗位ID不合法");
		}
		validateCreate(bo);
	}
}
