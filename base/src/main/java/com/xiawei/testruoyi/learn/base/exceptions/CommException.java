package com.xiawei.testruoyi.learn.base.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目公共异常类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class CommException extends RuntimeException {

	/** 异常对应的错误码 */
	private Integer code;

	public CommException(Integer code) {
		this.code = code;
	}

	public CommException(String message, Integer code) {
		super(message);
		this.code = code;
	}

	public CommException(String message, Throwable cause, Integer code) {
		super(message, cause);
		this.code = code;
	}

	/**
	 * 判断当前异常是否为致命异常
	 * @return true表示致命异常，false表示可恢复异常
	 */
	public abstract boolean isFatal();
}
