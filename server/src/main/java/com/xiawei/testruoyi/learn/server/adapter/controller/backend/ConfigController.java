package com.xiawei.testruoyi.learn.server.adapter.controller.backend;

import com.xiawei.testruoyi.learn.base.basecls.BaseController;
import com.xiawei.testruoyi.learn.base.dto.ApiResponse;
import com.xiawei.testruoyi.learn.base.dto.PageDTO;
import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.sdk.cmd.config.ConfigCmd;
import com.xiawei.testruoyi.learn.sdk.dto.config.ConfigDTO;
import com.xiawei.testruoyi.learn.server.application.bo.config.ConfigBO;
import com.xiawei.testruoyi.learn.server.application.service.ConfigAppService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.ConfigConvertor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 参数配置管理控制器
 */
@RestController
@RequestMapping("/backend/system/config")
@Tag(name = "后端接口-参数配置管理", description = "提供参数配置管理相关接口")
@RequiredArgsConstructor
public class ConfigController extends BaseController {

	private final ConfigAppService configAppService;
	private final ConfigConvertor configConvertor;

	@PostMapping
	@Operation(summary = "新增参数配置")
	public ApiResponse<Long> create(@RequestBody @Valid ConfigCmd cmd) {
		return succ(configAppService.createConfig(configConvertor.toCreateBO(cmd)));
	}

	@PutMapping
	@Operation(summary = "修改参数配置")
	public ApiResponse<Void> update(@RequestBody @Valid ConfigCmd cmd) {
		configAppService.updateConfig(cmd.getId(), configConvertor.toCreateBO(cmd));
		return succ();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除参数配置")
	public ApiResponse<Void> delete(@PathVariable Long id) {
		configAppService.deleteConfig(id);
		return succ();
	}

	@GetMapping("/{id}")
	@Operation(summary = "参数配置详情")
	public ApiResponse<ConfigDTO> getById(@PathVariable Long id) {
		return succ(configConvertor.toDTOFromBO(configAppService.getConfigById(id)));
	}

	@GetMapping("/list")
	@Operation(summary = "参数配置列表")
	public ApiResponse<PageDTO<ConfigDTO>> list(
			@RequestParam(defaultValue = "1") long current,
			@RequestParam(defaultValue = "10") long size) {
		PageBO<ConfigBO> boPage = configAppService.pageConfigs(current, size);
		return succ(PageDTO.from(boPage, configConvertor.toDTOFromBOList(boPage.getRecords())));
	}

	@GetMapping("/key/{configKey}")
	@Operation(summary = "按键查参数配置")
	public ApiResponse<ConfigDTO> getByKey(@PathVariable String configKey) {
		return succ(configConvertor.toDTOFromBO(configAppService.getConfigByKey(configKey)));
	}
}
