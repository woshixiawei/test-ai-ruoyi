package com.xiawei.testruoyi.learn.server.application.service;

import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.server.application.bo.log.LoginLogBO;

/**
 * 登录日志应用服务接口
 *
 * <p>入参和出参对象类型必须以 BO 结尾，禁止暴露Cmd/DTO等其他类型</p>
 */
public interface LoginLogAppService {

	/** 登录日志分页列表 */
	PageBO<LoginLogBO> pageLoginLogs(long current, long size);

	/** 清理所有登录日志 */
	void cleanAll();
}
