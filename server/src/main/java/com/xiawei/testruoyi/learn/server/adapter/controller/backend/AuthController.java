package com.xiawei.testruoyi.learn.server.adapter.controller.backend;

import com.xiawei.testruoyi.learn.base.basecls.BaseController;
import com.xiawei.testruoyi.learn.base.dto.ApiResponse;
import com.xiawei.testruoyi.learn.sdk.cmd.user.LoginCmd;
import com.xiawei.testruoyi.learn.sdk.dto.menu.MenuDTO;
import com.xiawei.testruoyi.learn.sdk.dto.user.LoginDTO;
import com.xiawei.testruoyi.learn.sdk.dto.user.UserDTO;
import com.xiawei.testruoyi.learn.server.application.service.AuthAppService;
import com.xiawei.testruoyi.learn.server.application.bo.menu.MenuBO;
import com.xiawei.testruoyi.learn.server.application.bo.user.UserBO;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.MenuConvertor;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.UserConvertor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 认证控制器
 *
 * <p>只调用AuthAppService，不直接访问 Repository 或 DomainService</p>
 */
@RestController
@RequestMapping("/backend/auth")
@Tag(name = "后端接口-认证授权", description = "提供登录登出等认证接口")
@RequiredArgsConstructor
public class AuthController extends BaseController {

	private final AuthAppService authAppService;
	private final UserConvertor userConvertor;
	private final MenuConvertor menuConvertor;

	/**
	 * 用户登录
	 */
	@PostMapping("/login")
	@Operation(summary = "用户登录")
	public ApiResponse<LoginDTO> login(@RequestBody @Valid LoginCmd cmd) {
		return succ(userConvertor.toDTOFromLoginBO(authAppService.login(userConvertor.toLoginBO(cmd))));
	}

	/**
	 * 用户登出
	 */
	@PostMapping("/logout")
	@Operation(summary = "用户登出")
	public ApiResponse<Void> logout() {
		authAppService.logout();
		return succ();
	}

	/**
	 * 获取当前登录用户信息
	 */
	@GetMapping("/info")
	@Operation(summary = "获取当前用户信息")
	public ApiResponse<UserDTO> getCurrentUser() {
		UserBO userBO = authAppService.getCurrentUserInfo();
		UserDTO dto = userConvertor.toDTOFromBO(userBO);
		// 附加角色和权限信息
		dto.setRoles(authAppService.getCurrentUserRoles());
		dto.setPermissions(authAppService.getCurrentUserPermissions());
		return succ(dto);
	}

	/**
	 * 获取当前用户路由/菜单
	 */
	@GetMapping("/routes")
	@Operation(summary = "获取当前用户路由")
	public ApiResponse<List<MenuDTO>> getRoutes() {
		List<MenuBO> menus = authAppService.getCurrentUserRoutes();
		return succ(menuConvertor.toDTOFromBOList(menus));
	}

	/**
	 * 获取当前用户权限标识
	 */
	@GetMapping("/permissions")
	@Operation(summary = "获取当前用户权限标识")
	public ApiResponse<List<String>> getPermissions() {
		return succ(authAppService.getCurrentUserPermissions());
	}
}
