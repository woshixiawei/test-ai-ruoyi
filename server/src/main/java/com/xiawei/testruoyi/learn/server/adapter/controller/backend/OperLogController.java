package com.xiawei.testruoyi.learn.server.adapter.controller.backend;

import com.xiawei.testruoyi.learn.base.basecls.BaseController;
import com.xiawei.testruoyi.learn.base.dto.ApiResponse;
import com.xiawei.testruoyi.learn.base.dto.PageDTO;
import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.sdk.dto.log.OperLogDTO;
import com.xiawei.testruoyi.learn.server.application.bo.log.OperLogBO;
import com.xiawei.testruoyi.learn.server.application.service.OperLogAppService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.OperLogConvertor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志管理控制器
 *
 * <p>只调用OperLogAppService，不直接访问 DomainService、Repository 或 Convertor</p>
 */
@RestController
@RequestMapping("/backend/system/operLog")
@Tag(name = "后端接口-操作日志管理", description = "提供操作日志查询相关接口")
@RequiredArgsConstructor
public class OperLogController extends BaseController {

	private final OperLogAppService operLogAppService;
	private final OperLogConvertor operLogConvertor;

	@GetMapping("/list")
	@Operation(summary = "操作日志列表")
	public ApiResponse<PageDTO<OperLogDTO>> list(
			@RequestParam(defaultValue = "1") long current,
			@RequestParam(defaultValue = "10") long size) {
		PageBO<OperLogBO> boPage = operLogAppService.pageOperLogs(current, size);
		return succ(PageDTO.from(boPage, operLogConvertor.toDTOFromBOList(boPage.getRecords())));
	}

	@DeleteMapping("/clean")
	@Operation(summary = "清理操作日志")
	public ApiResponse<Void> clean() {
		operLogAppService.cleanAll();
		return succ();
	}

	@GetMapping("/export")
	@Operation(summary = "导出操作日志")
	public ApiResponse<Void> export() {
		// TODO: 导出功能待实现，依赖Excel工具类
		return succ();
	}
}
