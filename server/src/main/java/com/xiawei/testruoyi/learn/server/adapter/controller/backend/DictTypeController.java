package com.xiawei.testruoyi.learn.server.adapter.controller.backend;

import com.xiawei.testruoyi.learn.base.basecls.BaseController;
import com.xiawei.testruoyi.learn.base.dto.ApiResponse;
import com.xiawei.testruoyi.learn.base.dto.PageDTO;
import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.sdk.cmd.dict.DictTypeCmd;
import com.xiawei.testruoyi.learn.sdk.dto.dict.DictTypeDTO;
import com.xiawei.testruoyi.learn.server.application.bo.dict.DictTypeBO;
import com.xiawei.testruoyi.learn.server.application.service.DictTypeAppService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.DictTypeConvertor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 字典类型管理控制器
 */
@RestController
@RequestMapping("/backend/system/dict/type")
@Tag(name = "后端接口-字典类型管理", description = "提供字典类型管理相关接口")
@RequiredArgsConstructor
public class DictTypeController extends BaseController {

	private final DictTypeAppService dictTypeAppService;
	private final DictTypeConvertor dictTypeConvertor;

	@PostMapping
	@Operation(summary = "新增字典类型")
	public ApiResponse<Long> create(@RequestBody @Valid DictTypeCmd cmd) {
		return succ(dictTypeAppService.createDictType(dictTypeConvertor.toCreateBO(cmd)));
	}

	@PutMapping
	@Operation(summary = "修改字典类型")
	public ApiResponse<Void> update(@RequestBody @Valid DictTypeCmd cmd) {
		dictTypeAppService.updateDictType(cmd.getId(), dictTypeConvertor.toCreateBO(cmd));
		return succ();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除字典类型")
	public ApiResponse<Void> delete(@PathVariable Long id) {
		dictTypeAppService.deleteDictType(id);
		return succ();
	}

	@GetMapping("/{id}")
	@Operation(summary = "字典类型详情")
	public ApiResponse<DictTypeDTO> getById(@PathVariable Long id) {
		return succ(dictTypeConvertor.toDTOFromBO(dictTypeAppService.getDictTypeById(id)));
	}

	@GetMapping("/list")
	@Operation(summary = "字典类型列表")
	public ApiResponse<PageDTO<DictTypeDTO>> list(
			@RequestParam(defaultValue = "1") long current,
			@RequestParam(defaultValue = "10") long size) {
		PageBO<DictTypeBO> boPage = dictTypeAppService.pageDictTypes(current, size);
		return succ(PageDTO.from(boPage, dictTypeConvertor.toDTOFromBOList(boPage.getRecords())));
	}
}
