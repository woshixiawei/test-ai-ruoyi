package com.xiawei.testruoyi.learn.sdk.cmd.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 更新用户命令
 */
@Data
@Schema(description = "更新用户请求")
public class UserUpdateCmd {

	@NotNull(message = "用户ID不能为空")
	@Schema(description = "用户ID")
	private Long id;

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

	@Schema(description = "所属部门ID")
	private Long deptId;

	@Schema(description = "岗位ID")
	private Long postId;

	@Schema(description = "角色ID列表")
	private List<Long> roleIds;

	@Schema(description = "备注")
	private String remark;
}
