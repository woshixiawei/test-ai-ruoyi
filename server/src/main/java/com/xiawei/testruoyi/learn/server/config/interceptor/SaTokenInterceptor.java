package com.xiawei.testruoyi.learn.server.config.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import com.xiawei.testruoyi.learn.base.constant.ErrConstant;
import com.xiawei.testruoyi.learn.base.dto.ApiResponse;
import com.xiawei.testruoyi.learn.base.util.JacksonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * sa-token 认证拦截器
 *
 * <p>统一返回 HTTP 200 + 业务错误码，禁止使用 HTTP 401/403。</p>
 */
@Slf4j
@Component
public class SaTokenInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
			// 校验登录
			StpUtil.checkLogin();
			return true;
		} catch (cn.dev33.satoken.exception.NotLoginException e) {
			log.warn("[SaTokenInterceptor] 未登录访问: uri={}", request.getRequestURI());
			writeErrorResponse(response, ErrConstant.NOT_LOGIN_ERR_CODE, "请先登录");
			return false;
		} catch (cn.dev33.satoken.exception.NotPermissionException e) {
			log.warn("[SaTokenInterceptor] 无权限访问: uri={}", request.getRequestURI());
			writeErrorResponse(response, ErrConstant.NO_PERMISSION_CODE, "无权限访问");
			return false;
		}
	}

	/**
	 * 统一错误响应：HTTP 200 + 业务错误码
	 */
	private void writeErrorResponse(HttpServletResponse response, int code, String msg) throws Exception {
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json;charset=UTF-8");
		ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
				.code(code)
				.msg(msg)
				.build();
		response.getWriter().write(JacksonUtil.toJsonStr(apiResponse));
	}
}
