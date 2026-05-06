package com.xiawei.testruoyi.learn.server.adapter.controller.backend;

import com.xiawei.testruoyi.learn.base.basecls.BaseController;
import com.xiawei.testruoyi.learn.base.dto.ApiResponse;
import com.xiawei.testruoyi.learn.sdk.cmd.menu.MenuCmd;
import com.xiawei.testruoyi.learn.sdk.dto.menu.MenuDTO;
import com.xiawei.testruoyi.learn.server.application.service.MenuAppService;
import com.xiawei.testruoyi.learn.server.application.bo.menu.MenuBO;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.MenuConvertor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理控制器
 */
@RestController
@RequestMapping("/backend/system/menu")
@Tag(name = "后端接口-菜单管理", description = "提供菜单管理相关接口")
@RequiredArgsConstructor
public class MenuController extends BaseController {

	private final MenuAppService menuAppService;
	private final MenuConvertor menuConvertor;

	@PostMapping
	@Operation(summary = "新增菜单")
	public ApiResponse<Long> create(@RequestBody @Valid MenuCmd cmd) {
		return succ(menuAppService.createMenu(menuConvertor.toCreateBO(cmd)));
	}

	@PutMapping
	@Operation(summary = "修改菜单")
	public ApiResponse<Void> update(@RequestBody @Valid MenuCmd cmd) {
		menuAppService.updateMenu(cmd.getId(), menuConvertor.toCreateBO(cmd));
		return succ();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除菜单")
	public ApiResponse<Void> delete(@PathVariable Long id) {
		menuAppService.deleteMenu(id);
		return succ();
	}

	@GetMapping("/{id}")
	@Operation(summary = "菜单详情")
	public ApiResponse<MenuDTO> getById(@PathVariable Long id) {
		return succ(menuConvertor.toDTOFromBO(menuAppService.getMenuById(id)));
	}

	@GetMapping("/list")
	@Operation(summary = "菜单列表")
	public ApiResponse<List<MenuDTO>> list() {
		return succ(menuConvertor.toDTOFromBOList(menuAppService.listMenus()));
	}

	@GetMapping("/tree")
	@Operation(summary = "菜单树")
	public ApiResponse<List<MenuDTO>> tree() {
		return succ(menuConvertor.toDTOFromBOList(menuAppService.menuTree()));
	}
}
