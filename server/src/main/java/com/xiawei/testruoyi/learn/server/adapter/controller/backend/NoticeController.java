package com.xiawei.testruoyi.learn.server.adapter.controller.backend;

import cn.dev33.satoken.stp.StpUtil;
import com.xiawei.testruoyi.learn.base.basecls.BaseController;
import com.xiawei.testruoyi.learn.base.dto.ApiResponse;
import com.xiawei.testruoyi.learn.base.dto.PageDTO;
import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.sdk.cmd.notice.NoticeCmd;
import com.xiawei.testruoyi.learn.sdk.dto.notice.NoticeDTO;
import com.xiawei.testruoyi.learn.server.application.bo.notice.NoticeBO;
import com.xiawei.testruoyi.learn.server.application.service.NoticeAppService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.NoticeConvertor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知公告管理控制器
 */
@RestController
@RequestMapping("/backend/system/notice")
@Tag(name = "后端接口-通知公告管理", description = "提供通知公告管理相关接口")
@RequiredArgsConstructor
public class NoticeController extends BaseController {

	private final NoticeAppService noticeAppService;
	private final NoticeConvertor noticeConvertor;

	@PostMapping
	@Operation(summary = "新增公告")
	public ApiResponse<Long> create(@RequestBody @Valid NoticeCmd cmd) {
		return succ(noticeAppService.createNotice(noticeConvertor.toCreateBO(cmd)));
	}

	@PutMapping
	@Operation(summary = "修改公告")
	public ApiResponse<Void> update(@RequestBody @Valid NoticeCmd cmd) {
		noticeAppService.updateNotice(cmd.getId(), noticeConvertor.toCreateBO(cmd));
		return succ();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "删除公告")
	public ApiResponse<Void> delete(@PathVariable Long id) {
		noticeAppService.deleteNotice(id);
		return succ();
	}

	@GetMapping("/{id}")
	@Operation(summary = "公告详情")
	public ApiResponse<NoticeDTO> getById(@PathVariable Long id) {
		return succ(noticeConvertor.toDTOFromBO(noticeAppService.getNoticeById(id)));
	}

	@GetMapping("/list")
	@Operation(summary = "公告列表")
	public ApiResponse<PageDTO<NoticeDTO>> list(
			@RequestParam(defaultValue = "1") long current,
			@RequestParam(defaultValue = "10") long size) {
		PageBO<NoticeBO> boPage = noticeAppService.pageNotices(current, size);
		return succ(PageDTO.from(boPage, noticeConvertor.toDTOFromBOList(boPage.getRecords())));
	}

	@PutMapping("/{id}/publish")
	@Operation(summary = "发布公告")
	public ApiResponse<Void> publish(@PathVariable Long id) {
		noticeAppService.publishNotice(id);
		return succ();
	}

	@PutMapping("/{id}/withdraw")
	@Operation(summary = "撤回公告")
	public ApiResponse<Void> withdraw(@PathVariable Long id) {
		noticeAppService.withdrawNotice(id);
		return succ();
	}

	@GetMapping("/unread")
	@Operation(summary = "未读公告列表")
	public ApiResponse<List<NoticeDTO>> unread() {
		Long userId = StpUtil.getLoginIdAsLong();
		List<NoticeBO> boList = noticeAppService.listUnreadNotices(userId);
		return succ(noticeConvertor.toDTOFromBOList(boList));
	}

	@PutMapping("/{id}/read")
	@Operation(summary = "标记已读")
	public ApiResponse<Void> markAsRead(@PathVariable Long id) {
		Long userId = StpUtil.getLoginIdAsLong();
		noticeAppService.markNoticeAsRead(id, userId);
		return succ();
	}
}
