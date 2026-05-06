package com.xiawei.testruoyi.learn.server.adapter.controller.backend;

import com.xiawei.testruoyi.learn.base.basecls.BaseController;
import com.xiawei.testruoyi.learn.base.dto.ApiResponse;
import com.xiawei.testruoyi.learn.base.dto.PageDTO;
import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.sdk.dto.file.FileDTO;
import com.xiawei.testruoyi.learn.server.application.bo.file.FileBO;
import com.xiawei.testruoyi.learn.server.application.service.FileAppService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.FileConvertor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件管理控制器
 *
 * <p>只调用FileAppService，不直接访问 DomainService、Repository 或 Convertor</p>
 */
@RestController
@RequestMapping("/backend/system/file")
@Tag(name = "后端接口-文件管理", description = "提供文件上传下载相关接口")
@RequiredArgsConstructor
public class FileController extends BaseController {

	private final FileAppService fileAppService;
	private final FileConvertor fileConvertor;

	@PostMapping("/upload")
	@Operation(summary = "文件上传")
	public ApiResponse<FileDTO> upload(@RequestParam("file") MultipartFile file) {
		FileBO bo = fileAppService.upload(file);
		return succ(fileConvertor.toDTOFromBO(bo));
	}

	@GetMapping("/list")
	@Operation(summary = "文件列表")
	public ApiResponse<PageDTO<FileDTO>> list(
			@RequestParam(defaultValue = "1") long current,
			@RequestParam(defaultValue = "10") long size) {
		PageBO<FileBO> boPage = fileAppService.pageFiles(current, size);
		return succ(PageDTO.from(boPage, fileConvertor.toDTOFromBOList(boPage.getRecords())));
	}

	@GetMapping("/{id}")
	@Operation(summary = "文件详情")
	public ApiResponse<FileDTO> getById(@PathVariable Long id) {
		return succ(fileConvertor.toDTOFromBO(fileAppService.getFileById(id)));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除文件")
	public ApiResponse<Void> delete(@PathVariable Long id) {
		fileAppService.deleteFile(id);
		return succ();
	}

	@GetMapping("/download/{id}")
	@Operation(summary = "下载文件")
	public void download(@PathVariable String id) {
		// TODO: 实现文件下载，依赖 HttpServletResponse 写入文件流
	}
}
