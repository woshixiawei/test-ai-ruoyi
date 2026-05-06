package com.xiawei.testruoyi.learn.base.exceptions;

import com.xiawei.testruoyi.learn.base.constant.ErrConstant;

/**
 * 业务异常（用户可理解，如"库存不足"）
 *
 * <p>isFatal=false，属于正常业务流程，仅记录日志不告警。</p>
 */
public class BusinessException extends CommException {

	public BusinessException(String message) {
		super(message, ErrConstant.BUSINESS_ERR_CODE);
	}

	public BusinessException(Integer code, String message) {
		super(message, code);
	}

	@Override
	public boolean isFatal() {
		return false;
	}
}
