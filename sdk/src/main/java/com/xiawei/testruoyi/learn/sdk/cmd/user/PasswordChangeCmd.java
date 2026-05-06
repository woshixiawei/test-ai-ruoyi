package com.xiawei.testruoyi.learn.sdk.cmd.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 密码修改命令
 */
@Data
@Schema(description = "密码修改命令")
public class PasswordChangeCmd {

	@NotBlank(message = "旧密码不能为空")
	@Schema(description = "旧密码")
	private String oldPassword;

	@NotBlank(message = "新密码不能为空")
	@Size(min = 6, max = 30, message = "新密码长度必须在6-30个字符之间")
	@Schema(description = "新密码")
	private String newPassword;
}
