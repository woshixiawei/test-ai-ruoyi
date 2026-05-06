package com.xiawei.testruoyi.learn.server.adapter.controller.backend;

import com.xiawei.testruoyi.learn.base.basecls.BaseController;
import com.xiawei.testruoyi.learn.base.dto.ApiResponse;
import com.xiawei.testruoyi.learn.base.dto.PageDTO;
import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.sdk.cmd.user.PasswordChangeCmd;
import com.xiawei.testruoyi.learn.sdk.cmd.user.ProfileUpdateCmd;
import com.xiawei.testruoyi.learn.sdk.dto.log.LoginLogDTO;
import com.xiawei.testruoyi.learn.sdk.dto.notice.NoticeDTO;
import com.xiawei.testruoyi.learn.sdk.dto.user.UserDTO;
import com.xiawei.testruoyi.learn.server.application.bo.log.LoginLogBO;
import com.xiawei.testruoyi.learn.server.application.bo.notice.NoticeBO;
import com.xiawei.testruoyi.learn.server.application.bo.user.ProfileBO;
import com.xiawei.testruoyi.learn.server.application.bo.user.UserCreateBO;
import com.xiawei.testruoyi.learn.server.application.service.ProfileAppService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.LoginLogConvertor;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.NoticeConvertor;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.UserConvertor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 个人中心控制器
 *
 * <p>只调用ProfileAppService，不直接访问 DomainService、Repository 或 Convertor</p>
 */
@RestController
@RequestMapping("/backend/system/profile")
@Tag(name = "后端接口-个人中心", description = "提供个人信息管理相关接口")
@RequiredArgsConstructor
public class ProfileController extends BaseController {

	private final ProfileAppService profileAppService;
	private final UserConvertor userConvertor;
	private final LoginLogConvertor loginLogConvertor;
	private final NoticeConvertor noticeConvertor;

	@GetMapping
	@Operation(summary = "获取个人信息")
	public ApiResponse<UserDTO> getProfile() {
		ProfileBO bo = profileAppService.getProfile();
		return succ(userConvertor.toDTOFromProfileBO(bo));
	}

	@PutMapping
	@Operation(summary = "修改个人信息")
	public ApiResponse<Void> updateProfile(@RequestBody @Valid ProfileUpdateCmd cmd) {
		UserCreateBO createBO = userConvertor.toProfileUpdateBO(cmd);
		profileAppService.updateProfile(createBO);
		return succ();
	}

	@PutMapping("/password")
	@Operation(summary = "修改密码")
	public ApiResponse<Void> changePassword(@RequestBody @Valid PasswordChangeCmd cmd) {
		profileAppService.changePassword(cmd.getOldPassword(), cmd.getNewPassword());
		return succ();
	}

	@PostMapping("/avatar")
	@Operation(summary = "上传头像")
	public ApiResponse<Void> uploadAvatar() {
		// TODO: 头像上传，复用文件上传逻辑
		return succ();
	}

	@GetMapping("/loginLog")
	@Operation(summary = "个人登录历史")
	public ApiResponse<PageDTO<LoginLogDTO>> loginLog(
			@RequestParam(defaultValue = "1") long current,
			@RequestParam(defaultValue = "10") long size) {
		PageBO<LoginLogBO> boPage = profileAppService.pageLoginLogs(current, size);
		return succ(PageDTO.from(boPage, loginLogConvertor.toDTOFromBOList(boPage.getRecords())));
	}

	@GetMapping("/messages")
	@Operation(summary = "个人消息列表")
	public ApiResponse<List<NoticeDTO>> messages() {
		List<NoticeBO> boList = profileAppService.listMessages();
		return succ(noticeConvertor.toDTOFromBOList(boList));
	}
}
