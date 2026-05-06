package com.xiawei.testruoyi.learn.sdk.cmd.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 创建用户命令
 */
@Data
@Schema(description = "创建用户请求")
public class UserCreateCmd {

	@NotBlank(message = "用户名不能为空")
	@Size(max = 64, message = "用户名长度不能超过64个字符")
	@Schema(description = "用户名")
	private String username;

	@Size(max = 64, message = "昵称长度不能超过64个字符")
	@Schema(description = "用户昵称")
	private String nickname;

	@Email(message = "邮箱格式不正确")
	@Size(max = 128, message = "邮箱长度不能超过128个字符")
	@Schema(description = "邮箱")
	private String email;

	@Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
	@Schema(description = "手机号")
	private String phone;

	@NotBlank(message = "密码不能为空")
	@Size(min = 6, max = 32, message = "密码长度必须在6-32个字符之间")
	@Schema(description = "密码")
	private String password;

	@Schema(description = "所属部门ID")
	private Long deptId;

	@Schema(description = "岗位ID")
	private Long postId;

	@Schema(description = "角色ID列表")
	private List<Long> roleIds;

	@Schema(description = "备注")
	private String remark;
}
