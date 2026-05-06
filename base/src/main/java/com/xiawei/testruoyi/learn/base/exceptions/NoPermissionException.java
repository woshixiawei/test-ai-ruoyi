package com.xiawei.testruoyi.learn.base.exceptions;

import com.xiawei.testruoyi.learn.base.constant.ErrConstant;

/**
 * 无权限异常
 *
 * <p>isFatal=false，属于正常业务流程，仅记录日志不告警。</p>
 */
public class NoPermissionException extends CommException {

	public NoPermissionException() {
		super("无权限访问", ErrConstant.NO_PERMISSION_CODE);
	}

	public NoPermissionException(String message) {
		super(message, ErrConstant.NO_PERMISSION_CODE);
	}

	@Override
	public boolean isFatal() {
		return false;
	}
}
