package com.xiawei.testruoyi.learn.server.adapter.controller.backend;

import com.xiawei.testruoyi.learn.base.basecls.BaseController;
import com.xiawei.testruoyi.learn.base.dto.ApiResponse;
import com.xiawei.testruoyi.learn.sdk.cmd.dept.DeptCmd;
import com.xiawei.testruoyi.learn.sdk.dto.dept.DeptDTO;
import com.xiawei.testruoyi.learn.server.application.service.DeptAppService;
import com.xiawei.testruoyi.learn.server.application.bo.dept.DeptBO;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.DeptConvertor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 */
@RestController
@RequestMapping("/backend/system/dept")
@Tag(name = "后端接口-部门管理", description = "提供部门管理相关接口")
@RequiredArgsConstructor
public class DeptController extends BaseController {

	private final DeptAppService deptAppService;
	private final DeptConvertor deptConvertor;

	@PostMapping
	@Operation(summary = "新增部门")
	public ApiResponse<Long> create(@RequestBody @Valid DeptCmd cmd) {
		return succ(deptAppService.createDept(deptConvertor.toCreateBO(cmd)));
	}

	@PutMapping
	@Operation(summary = "修改部门")
	public ApiResponse<Void> update(@RequestBody @Valid DeptCmd cmd) {
		deptAppService.updateDept(cmd.getId(), deptConvertor.toCreateBO(cmd));
		return succ();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除部门")
	public ApiResponse<Void> delete(@PathVariable Long id) {
		deptAppService.deleteDept(id);
		return succ();
	}

	@GetMapping("/{id}")
	@Operation(summary = "部门详情")
	public ApiResponse<DeptDTO> getById(@PathVariable Long id) {
		return succ(deptConvertor.toDTOFromBO(deptAppService.getDeptById(id)));
	}

	@GetMapping("/list")
	@Operation(summary = "部门列表")
	public ApiResponse<List<DeptDTO>> list() {
		return succ(deptConvertor.toDTOFromBOList(deptAppService.listDepts()));
	}

	@GetMapping("/tree")
	@Operation(summary = "部门树")
	public ApiResponse<List<DeptDTO>> tree() {
		return succ(deptConvertor.toDTOFromBOList(deptAppService.deptTree()));
	}
}
