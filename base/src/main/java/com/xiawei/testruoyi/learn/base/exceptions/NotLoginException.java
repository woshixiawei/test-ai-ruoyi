package com.xiawei.testruoyi.learn.base.exceptions;

import com.xiawei.testruoyi.learn.base.constant.ErrConstant;

/**
 * 未登录异常
 *
 * <p>isFatal=false，属于正常业务流程，仅记录日志不告警。</p>
 */
public class NotLoginException extends CommException {

	public NotLoginException() {
		super("请先登录", ErrConstant.NOT_LOGIN_ERR_CODE);
	}

	public NotLoginException(String message) {
		super(message, ErrConstant.NOT_LOGIN_ERR_CODE);
	}

	@Override
	public boolean isFatal() {
		return false;
	}
}
