package com.xiawei.testruoyi.learn.server.adapter.controller.backend;

import com.xiawei.testruoyi.learn.base.basecls.BaseController;
import com.xiawei.testruoyi.learn.base.dto.ApiResponse;
import com.xiawei.testruoyi.learn.base.dto.PageDTO;
import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.sdk.dto.log.LoginLogDTO;
import com.xiawei.testruoyi.learn.server.application.bo.log.LoginLogBO;
import com.xiawei.testruoyi.learn.server.application.service.LoginLogAppService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.LoginLogConvertor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 登录日志管理控制器
 *
 * <p>只调用LoginLogAppService，不直接访问 DomainService、Repository 或 Convertor</p>
 */
@RestController
@RequestMapping("/backend/system/loginLog")
@Tag(name = "后端接口-登录日志管理", description = "提供登录日志查询相关接口")
@RequiredArgsConstructor
public class LoginLogController extends BaseController {

	private final LoginLogAppService loginLogAppService;
	private final LoginLogConvertor loginLogConvertor;

	@GetMapping("/list")
	@Operation(summary = "登录日志列表")
	public ApiResponse<PageDTO<LoginLogDTO>> list(
			@RequestParam(defaultValue = "1") long current,
			@RequestParam(defaultValue = "10") long size) {
		PageBO<LoginLogBO> boPage = loginLogAppService.pageLoginLogs(current, size);
		return succ(PageDTO.from(boPage, loginLogConvertor.toDTOFromBOList(boPage.getRecords())));
	}

	@DeleteMapping("/clean")
	@Operation(summary = "清理登录日志")
	public ApiResponse<Void> clean() {
		loginLogAppService.cleanAll();
		return succ();
	}

	@GetMapping("/export")
	@Operation(summary = "导出登录日志")
	public ApiResponse<Void> export() {
		// TODO: 导出功能待实现，依赖Excel工具类
		return succ();
	}
}
