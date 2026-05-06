package com.xiawei.testruoyi.learn.server.adapter.controller.backend;

import com.xiawei.testruoyi.learn.base.basecls.BaseController;
import com.xiawei.testruoyi.learn.base.dto.ApiResponse;
import com.xiawei.testruoyi.learn.base.dto.PageDTO;
import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.sdk.cmd.post.PostCmd;
import com.xiawei.testruoyi.learn.sdk.dto.post.PostDTO;
import com.xiawei.testruoyi.learn.server.application.service.PostAppService;
import com.xiawei.testruoyi.learn.server.application.bo.post.PostBO;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.PostConvertor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 岗位管理控制器
 */
@RestController
@RequestMapping("/backend/system/post")
@Tag(name = "后端接口-岗位管理", description = "提供岗位管理相关接口")
@RequiredArgsConstructor
public class PostController extends BaseController {

	private final PostAppService postAppService;
	private final PostConvertor postConvertor;

	@PostMapping
	@Operation(summary = "新增岗位")
	public ApiResponse<Long> create(@RequestBody @Valid PostCmd cmd) {
		return succ(postAppService.createPost(postConvertor.toCreateBO(cmd)));
	}

	@PutMapping
	@Operation(summary = "修改岗位")
	public ApiResponse<Void> update(@RequestBody @Valid PostCmd cmd) {
		postAppService.updatePost(cmd.getId(), postConvertor.toCreateBO(cmd));
		return succ();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除岗位")
	public ApiResponse<Void> delete(@PathVariable Long id) {
		postAppService.deletePost(id);
		return succ();
	}

	@GetMapping("/{id}")
	@Operation(summary = "岗位详情")
	public ApiResponse<PostDTO> getById(@PathVariable Long id) {
		return succ(postConvertor.toDTOFromBO(postAppService.getPostById(id)));
	}

	@GetMapping("/list")
	@Operation(summary = "岗位列表")
	public ApiResponse<PageDTO<PostDTO>> list(
			@RequestParam(defaultValue = "1") long current,
			@RequestParam(defaultValue = "10") long size) {
		PageBO<PostBO> boPage = postAppService.pagePosts(current, size);
		return succ(PageDTO.from(boPage, postConvertor.toDTOFromBOList(boPage.getRecords())));
	}
}
