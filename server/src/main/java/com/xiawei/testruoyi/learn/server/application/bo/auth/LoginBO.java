package com.xiawei.testruoyi.learn.server.application.bo.auth;

import lombok.Data;

/**
 * 登录业务对象
 */
@Data
public class LoginBO {

	/** 用户名 */
	private String username;

	/** 密码 */
	private String password;
}
