package com.xiawei.testruoyi.learn.base.basecls;

import com.xiawei.testruoyi.learn.base.dto.ApiResponse;
import com.xiawei.testruoyi.learn.base.dto.PageDTO;

import java.util.List;

/**
 * Controller 基类
 *
 * <p>提供统一的 succ 响应方法，所有 Controller 必须继承此类。</p>
 */
public abstract class BaseController {

	/**
	 * 成功响应（无数据）
	 */
	protected <T> ApiResponse<T> succ() {
		return ApiResponse.succ();
	}

	/**
	 * 成功响应（有数据）
	 */
	protected <T> ApiResponse<T> succ(T data) {
		return ApiResponse.succ(data);
	}

	/**
	 * 成功响应（分页数据）
	 */
	protected <T> ApiResponse<PageDTO<T>> succPage(long current, long size, long total, List<T> records) {
		PageDTO<T> pageDTO = new PageDTO<>();
		pageDTO.setCurrent(current);
		pageDTO.setSize(size);
		pageDTO.setTotal(total);
		pageDTO.setRecords(records);
		return ApiResponse.succ(pageDTO);
	}
}
