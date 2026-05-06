package com.xiawei.testruoyi.learn.server.config;

import com.xiawei.testruoyi.learn.server.config.interceptor.SaTokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final SaTokenInterceptor saTokenInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(saTokenInterceptor)
				.addPathPatterns("/backend/**")
				.excludePathPatterns(
						"/backend/auth/login",
						"/backend/auth/register",
						"/backend/system/dict/data/type/**",
						"/backend/system/config/key/**",
						"/v3/api-docs/**",
						"/swagger-ui/**",
						"/swagger-ui.html",
						"/doc.html",
						"/webjars/**",
						"/favicon.ico"
				);
	}
}
