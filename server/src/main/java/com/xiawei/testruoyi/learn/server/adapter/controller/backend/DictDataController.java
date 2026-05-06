package com.xiawei.testruoyi.learn.server.adapter.controller.backend;

import com.xiawei.testruoyi.learn.base.basecls.BaseController;
import com.xiawei.testruoyi.learn.base.dto.ApiResponse;
import com.xiawei.testruoyi.learn.base.dto.PageDTO;
import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.sdk.cmd.dict.DictDataCmd;
import com.xiawei.testruoyi.learn.sdk.dto.dict.DictDataDTO;
import com.xiawei.testruoyi.learn.server.application.bo.dict.DictDataBO;
import com.xiawei.testruoyi.learn.server.application.service.DictDataAppService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.DictDataConvertor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典数据管理控制器
 */
@RestController
@RequestMapping("/backend/system/dict/data")
@Tag(name = "后端接口-字典数据管理", description = "提供字典数据管理相关接口")
@RequiredArgsConstructor
public class DictDataController extends BaseController {

	private final DictDataAppService dictDataAppService;
	private final DictDataConvertor dictDataConvertor;

	@PostMapping
	@Operation(summary = "新增字典数据")
	public ApiResponse<Long> create(@RequestBody @Valid DictDataCmd cmd) {
		return succ(dictDataAppService.createDictData(dictDataConvertor.toCreateBO(cmd)));
	}

	@PutMapping
	@Operation(summary = "修改字典数据")
	public ApiResponse<Void> update(@RequestBody @Valid DictDataCmd cmd) {
		dictDataAppService.updateDictData(cmd.getId(), dictDataConvertor.toCreateBO(cmd));
		return succ();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除字典数据")
	public ApiResponse<Void> delete(@PathVariable Long id) {
		dictDataAppService.deleteDictData(id);
		return succ();
	}

	@GetMapping("/{id}")
	@Operation(summary = "字典数据详情")
	public ApiResponse<DictDataDTO> getById(@PathVariable Long id) {
		return succ(dictDataConvertor.toDTOFromBO(dictDataAppService.getDictDataById(id)));
	}

	@GetMapping("/list")
	@Operation(summary = "字典数据列表")
	public ApiResponse<PageDTO<DictDataDTO>> list(
			@RequestParam(defaultValue = "1") long current,
			@RequestParam(defaultValue = "10") long size,
			@RequestParam(required = false) Long dictTypeId) {
		PageBO<DictDataBO> boPage = dictDataAppService.pageDictData(current, size, dictTypeId);
		return succ(PageDTO.from(boPage, dictDataConvertor.toDTOFromBOList(boPage.getRecords())));
	}

	@GetMapping("/type/{dictType}")
	@Operation(summary = "按类型查字典数据")
	public ApiResponse<List<DictDataDTO>> listByType(@PathVariable String dictType) {
		List<DictDataBO> boList = dictDataAppService.listDictDataByType(dictType);
		return succ(dictDataConvertor.toDTOFromBOList(boList));
	}
}
