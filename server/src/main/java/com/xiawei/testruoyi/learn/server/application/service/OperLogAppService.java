package com.xiawei.testruoyi.learn.server.application.service;

import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.server.application.bo.log.OperLogBO;

/**
 * 操作日志应用服务接口
 *
 * <p>入参和出参对象类型必须以 BO 结尾，禁止暴露Cmd/DTO等其他类型</p>
 */
public interface OperLogAppService {

	/** 操作日志分页列表 */
	PageBO<OperLogBO> pageOperLogs(long current, long size);

	/** 清理所有操作日志 */
	void cleanAll();
}
