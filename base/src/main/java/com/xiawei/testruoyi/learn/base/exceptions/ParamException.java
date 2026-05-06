package com.xiawei.testruoyi.learn.base.exceptions;

import com.xiawei.testruoyi.learn.base.constant.ErrConstant;

/**
 * 参数校验异常
 *
 * <p>isFatal=false，属于正常业务流程，仅记录日志不告警。</p>
 */
public class ParamException extends CommException {

	public ParamException(String message) {
		super(message, ErrConstant.PARAM_ERR_CODE);
	}

	@Override
	public boolean isFatal() {
		return false;
	}
}
