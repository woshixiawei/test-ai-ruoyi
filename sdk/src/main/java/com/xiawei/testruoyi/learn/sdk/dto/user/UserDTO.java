package com.xiawei.testruoyi.learn.sdk.dto.user;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 用户DTO
 */
@Data
@Schema(description = "用户信息")
public class UserDTO {

	@Schema(description = "用户ID")
	private Long id;

	@Schema(description = "用户名，唯一")
	private String username;

	@Schema(description = "用户昵称")
	private String nickname;

	@Schema(description = "邮箱")
	private String email;

	@Schema(description = "手机号")
	private String phone;

	@Schema(description = "头像地址")
	private String avatar;

	@Schema(description = "状态：0-禁用，1-正常")
	private Integer status;

	@Schema(description = "所属部门ID")
	private Long deptId;

	@Schema(description = "岗位ID")
	private Long postId;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "角色ID列表")
	private List<Long> roleIds;

	@Schema(description = "角色标识列表")
	private List<String> roles;

	@Schema(description = "权限标识列表")
	private List<String> permissions;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "创建时间")
	private Date createTime;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "更新时间")
	private Date updateTime;
}
