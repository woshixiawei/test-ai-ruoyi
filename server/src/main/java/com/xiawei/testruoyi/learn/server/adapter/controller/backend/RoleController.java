package com.xiawei.testruoyi.learn.server.adapter.controller.backend;

import com.xiawei.testruoyi.learn.base.basecls.BaseController;
import com.xiawei.testruoyi.learn.base.dto.ApiResponse;
import com.xiawei.testruoyi.learn.base.dto.PageDTO;
import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.sdk.cmd.role.RoleCmd;
import com.xiawei.testruoyi.learn.sdk.dto.role.RoleDTO;
import com.xiawei.testruoyi.learn.server.application.service.RoleAppService;
import com.xiawei.testruoyi.learn.server.application.bo.role.RoleBO;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.RoleConvertor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/backend/system/role")
@Tag(name = "后端接口-角色管理", description = "提供角色管理相关接口")
@RequiredArgsConstructor
public class RoleController extends BaseController {

	private final RoleAppService roleAppService;
	private final RoleConvertor roleConvertor;

	@PostMapping
	@Operation(summary = "新增角色")
	public ApiResponse<Long> create(@RequestBody @Valid RoleCmd cmd) {
		return succ(roleAppService.createRole(roleConvertor.toCreateBO(cmd)));
	}

	@PutMapping
	@Operation(summary = "修改角色")
	public ApiResponse<Void> update(@RequestBody @Valid RoleCmd cmd) {
		// 更新场景通过cmd中的id标识，此处需要从BO中获取
		roleAppService.updateRole(cmd.getId(), roleConvertor.toCreateBO(cmd));
		return succ();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除角色")
	public ApiResponse<Void> delete(@PathVariable Long id) {
		roleAppService.deleteRole(id);
		return succ();
	}

	@GetMapping("/{id}")
	@Operation(summary = "角色详情")
	public ApiResponse<RoleDTO> getById(@PathVariable Long id) {
		return succ(roleConvertor.toDTOFromBO(roleAppService.getRoleById(id)));
	}

	@GetMapping("/list")
	@Operation(summary = "角色列表")
	public ApiResponse<PageDTO<RoleDTO>> list(
			@RequestParam(defaultValue = "1") long current,
			@RequestParam(defaultValue = "10") long size,
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Integer status) {
		PageBO<RoleBO> boPage = roleAppService.pageRoles(current, size, keyword, status);
		return succ(PageDTO.from(boPage, roleConvertor.toDTOFromBOList(boPage.getRecords())));
	}

	@GetMapping("/{id}/menus")
	@Operation(summary = "获取角色已分配菜单")
	public ApiResponse<List<Long>> getRoleMenus(@PathVariable Long id) {
		return succ(roleAppService.getRoleMenus(id));
	}

	@PutMapping("/{id}/menus")
	@Operation(summary = "分配角色菜单权限")
	public ApiResponse<Void> assignMenus(@PathVariable Long id, @RequestBody List<Long> menuIds) {
		roleAppService.assignRoleMenus(id, menuIds);
		return succ();
	}
}
