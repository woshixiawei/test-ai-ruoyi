package com.xiawei.testruoyi.learn.sdk.cmd.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 个人信息更新命令
 */
@Data
@Schema(description = "个人信息更新命令")
public class ProfileUpdateCmd {

	@Size(min = 1, max = 30, message = "昵称长度必须在1-30个字符之间")
	@Schema(description = "昵称")
	private String nickname;

	@Email(message = "邮箱格式不正确")
	@Schema(description = "邮箱")
	private String email;

	@Schema(description = "手机号")
	private String phone;
}
