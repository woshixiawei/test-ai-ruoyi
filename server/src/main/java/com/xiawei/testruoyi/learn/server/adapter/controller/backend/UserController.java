package com.xiawei.testruoyi.learn.server.adapter.controller.backend;

import com.xiawei.testruoyi.learn.base.basecls.BaseController;
import com.xiawei.testruoyi.learn.base.dto.ApiResponse;
import com.xiawei.testruoyi.learn.base.dto.PageDTO;
import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.sdk.cmd.user.ResetPasswordCmd;
import com.xiawei.testruoyi.learn.sdk.cmd.user.UpdateStatusCmd;
import com.xiawei.testruoyi.learn.sdk.cmd.user.UserCreateCmd;
import com.xiawei.testruoyi.learn.sdk.cmd.user.UserUpdateCmd;
import com.xiawei.testruoyi.learn.sdk.dto.user.UserDTO;
import com.xiawei.testruoyi.learn.server.application.service.UserAppService;
import com.xiawei.testruoyi.learn.server.application.bo.user.UserBO;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.UserConvertor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 *
 * <p>只调用UserAppService，不直接访问 DomainService、Repository 或 Convertor</p>
 */
@RestController
@RequestMapping("/backend/system/user")
@Tag(name = "后端接口-用户管理", description = "提供用户管理相关接口")
@RequiredArgsConstructor
public class UserController extends BaseController {

	private final UserAppService userAppService;
	private final UserConvertor userConvertor;

	/**
	 * 创建用户
	 */
	@PostMapping
	@Operation(summary = "创建用户")
	public ApiResponse<Long> create(@RequestBody @Valid UserCreateCmd cmd) {
		Long id = userAppService.createUser(userConvertor.toCreateBO(cmd));
		return succ(id);
	}

	/**
	 * 更新用户
	 */
	@PutMapping("/{id}")
	@Operation(summary = "更新用户")
	public ApiResponse<Void> update(@PathVariable Long id, @RequestBody @Valid UserUpdateCmd cmd) {
		userAppService.updateUser(id, userConvertor.toCreateBOFromUpdateCmd(cmd));
		return succ();
	}

	/**
	 * 删除用户
	 */
	@DeleteMapping("/{id}")
	@Operation(summary = "删除用户")
	public ApiResponse<Void> delete(@PathVariable Long id) {
		userAppService.deleteUser(id);
		return succ();
	}

	/**
	 * 根据ID查询用户
	 */
	@GetMapping("/{id}")
	@Operation(summary = "查询用户详情")
	public ApiResponse<UserDTO> getById(@PathVariable Long id) {
		return succ(userConvertor.toDTOFromBO(userAppService.getUserById(id)));
	}

	/**
	 * 分页查询用户列表
	 */
	@GetMapping("/list")
	@Operation(summary = "分页查询用户列表")
	public ApiResponse<PageDTO<UserDTO>> list(
			@RequestParam(defaultValue = "1") long current,
			@RequestParam(defaultValue = "10") long size,
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Integer status) {
		PageBO<UserBO> boPage = userAppService.pageUsers(current, size, keyword, status);
		return succ(PageDTO.from(boPage, userConvertor.toDTOFromBOList(boPage.getRecords())));
	}

	/**
	 * 重置用户密码
	 */
	@PutMapping("/{id}/password/reset")
	@Operation(summary = "重置用户密码")
	public ApiResponse<Void> resetPassword(@PathVariable Long id, @RequestBody ResetPasswordCmd cmd) {
		userAppService.resetPassword(id, cmd.getNewPassword());
		return succ();
	}

	/**
	 * 修改用户状态
	 */
	@PutMapping("/{id}/status")
	@Operation(summary = "修改用户状态")
	public ApiResponse<Void> updateStatus(@PathVariable Long id, @RequestBody UpdateStatusCmd cmd) {
		userAppService.updateStatus(id, cmd.getStatus());
		return succ();
	}
}
