package com.xiawei.testruoyi.learn.server.config.handler;

import com.xiawei.testruoyi.learn.base.constant.ErrConstant;
import com.xiawei.testruoyi.learn.base.dto.ApiResponse;
import com.xiawei.testruoyi.learn.base.exceptions.CommException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * <p>采用单入口 + instanceof 分发模式，所有异常统一进入 exceptionHandler 方法。</p>
 * <p>通过 CommException 的 isFatal() 判断是否为致命异常，致命异常记录到异常表并告警。</p>
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ApiResponse<Void> exceptionHandler(Exception exception) {
		String traceId = MDC.get("traceId");

		// 1. CommException 体系异常
		if (exception instanceof CommException commException) {
			if (commException.isFatal()) {
				log.error("发生致命异常", exception);
			} else {
				log.warn("发生非致命异常", exception);
			}
			return ApiResponse.<Void>builder()
					.code(commException.getCode())
					.msg(exception.getMessage())
					.traceId(traceId)
					.build();
		}

		// 2. sa-token 未登录异常
		if (exception instanceof cn.dev33.satoken.exception.NotLoginException) {
			log.warn("未登录异常", exception);
			return ApiResponse.<Void>builder()
					.code(ErrConstant.NOT_LOGIN_ERR_CODE)
					.msg("请先登录")
					.traceId(traceId)
					.build();
		}

		// 3. sa-token 无权限异常
		if (exception instanceof cn.dev33.satoken.exception.NotPermissionException) {
			log.warn("无权限异常", exception);
			return ApiResponse.<Void>builder()
					.code(ErrConstant.NO_PERMISSION_CODE)
					.msg("无权限访问")
					.traceId(traceId)
					.build();
		}

		// 4. 参数校验异常（@Valid / @Validated 触发）
		if (exception instanceof MethodArgumentNotValidException validException) {
			String message = validException.getBindingResult().getFieldErrors().stream()
					.map(error -> error.getField() + ": " + error.getDefaultMessage())
					.collect(Collectors.joining("; "));
			log.warn("[ValidationException] {}", message);
			return ApiResponse.<Void>builder()
					.code(ErrConstant.PARAM_ERR_CODE)
					.msg(message)
					.traceId(traceId)
					.build();
		}

		// 5. 参数绑定异常
		if (exception instanceof BindException bindException) {
			FieldError fieldError = bindException.getFieldError();
			String message = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";
			log.warn("[BindException] {}", message);
			return ApiResponse.<Void>builder()
					.code(ErrConstant.PARAM_ERR_CODE)
					.msg(message)
					.traceId(traceId)
					.build();
		}

		// 6. 兜底：未知异常
		log.error("发生未知异常", exception);
		return ApiResponse.<Void>builder()
				.code(ErrConstant.SYSTEM_ERR_CODE)
				.msg("系统繁忙，请稍后重试")
				.traceId(traceId)
				.build();
	}
}
