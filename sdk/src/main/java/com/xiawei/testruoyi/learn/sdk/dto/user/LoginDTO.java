package com.xiawei.testruoyi.learn.sdk.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 登录响应DTO
 */
@Data
@Schema(description = "登录响应")
public class LoginDTO {

	@Schema(description = "访问令牌")
	private String token;

	@Schema(description = "用户信息")
	private UserDTO userInfo;

	@Schema(description = "角色列表")
	private List<String> roles;

	@Schema(description = "权限标识列表")
	private List<String> permissions;
}
