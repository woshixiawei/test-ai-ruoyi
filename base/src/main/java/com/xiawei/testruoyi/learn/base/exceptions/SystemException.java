package com.xiawei.testruoyi.learn.base.exceptions;

import com.xiawei.testruoyi.learn.base.constant.ErrConstant;

/**
 * 系统异常（数据库连接失败等）
 *
 * <p>isFatal=true，需要人工介入，全局异常处理器会记录到异常表并告警。</p>
 */
public class SystemException extends CommException {

	public SystemException(String message) {
		super(message, ErrConstant.SYSTEM_ERR_CODE);
	}

	public SystemException(String message, Throwable cause) {
		super(message, cause, ErrConstant.SYSTEM_ERR_CODE);
	}

	public SystemException(Throwable cause) {
		super(cause.getMessage() != null ? cause.getMessage() : "系统异常", cause, ErrConstant.SYSTEM_ERR_CODE);
	}

	@Override
	public boolean isFatal() {
		return true;
	}
}
