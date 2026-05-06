package com.xiawei.testruoyi.learn.server.domain.user.service;

import com.xiawei.testruoyi.learn.base.exceptions.BusinessException;
import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.PostDO;
import com.xiawei.testruoyi.learn.server.domain.user.repository.PostRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.PostConvertor;
import com.xiawei.testruoyi.learn.server.infrastructure.po.PostPO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 岗位领域服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostDomainService {

	private final PostRepository postRepository;
	private final PostConvertor postConvertor;

	/** 保存岗位 */
	public void save(PostDO post) {
		PostPO po = postConvertor.toPO(post);
		postRepository.saveOrUpdate(po);
		post.setId(po.getId());
	}

	/** 根据ID查询岗位 */
	public PostDO getById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("岗位ID不合法");
		}
		PostPO po = postRepository.getById(id);
		if (po == null) {
			throw new BusinessException("岗位不存在");
		}
		return postConvertor.toDomain(po);
	}

	/** 校验岗位存在（数据存在性校验） */
	public PostDO validatePostExists(Long postId) {
		if (postId == null || postId <= 0) {
			throw new ParamException("岗位ID不合法");
		}
		PostPO po = postRepository.getById(postId);
		if (po == null) {
			throw new BusinessException("岗位不存在");
		}
		return postConvertor.toDomain(po);
	}

	/** 校验岗位编码唯一（业务规则校验） */
	public void validatePostCodeNotExists(String postCode, Long excludeId) {
		if (postCode == null || postCode.isBlank()) {
			throw new ParamException("岗位编码不能为空");
		}
		if (postRepository.existsByPostCode(postCode, excludeId)) {
			throw new BusinessException("岗位编码已存在");
		}
	}

	/** 校验岗位下无用户（删除前校验） */
	public void validateNoUsers(Long postId) {
		// 通过UserRepository检查岗位下是否有用户
		// 这里通过构造注入可以拿到UserMapper，但为保持领域服务纯净，交由AppService层校验
	}

	/** 删除岗位 */
	public void deleteById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("岗位ID不合法");
		}
		postRepository.removeById(id);
	}

	/** 分页查询岗位 */
	public PageDO<PostDO> findPage(long current, long size) {
		com.baomidou.mybatisplus.extension.plugins.pagination.Page<PostPO> poPage =
				postRepository.findPage(current, size);
		return PageDO.from(poPage, postConvertor.toDomainList(poPage.getRecords()));
	}
}
