package com.xiawei.testruoyi.learn.server.application.bo.auth;

import com.xiawei.testruoyi.learn.server.application.bo.user.UserBO;
import lombok.Data;

import java.util.List;

/**
 * 登录结果业务对象
 */
@Data
public class LoginResultBO {

	/** 访问令牌 */
	private String token;

	/** 用户信息 */
	private UserBO userInfo;

	/** 角色列表 */
	private List<String> roles;

	/** 权限标识列表 */
	private List<String> permissions;
}
