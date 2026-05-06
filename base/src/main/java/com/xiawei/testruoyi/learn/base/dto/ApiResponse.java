package com.xiawei.testruoyi.learn.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一 API 响应封装
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 错误码，成功为 0 */
	@Builder.Default
	private int code = 0;

	/** 错误消息提示 */
	@Builder.Default
	private String msg = null;

	/** 返回数据 */
	@Builder.Default
	private T data = null;

	/** 链路追踪ID */
	@Builder.Default
	private String traceId = null;

	public static <T> ApiResponse<T> succ(T data) {
		return ApiResponse.<T>builder().code(0).data(data).build();
	}

	public static <T> ApiResponse<T> succ() {
		return ApiResponse.<T>builder().code(0).build();
	}

	public static <T> ApiResponse<T> fail(int code, String msg) {
		return ApiResponse.<T>builder().code(code).msg(msg).build();
	}

	public boolean isSuccess() {
		return this.code == 0;
	}
}
