package com.xiawei.testruoyi.learn.server.application.bo.user;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 个人中心业务对象
 */
@Data
public class ProfileBO {

	/** 主键 */
	private Long id;

	/** 用户名 */
	private String username;

	/** 昵称 */
	private String nickname;

	/** 邮箱 */
	private String email;

	/** 手机号 */
	private String phone;

	/** 性别 */
	private Integer sex;

	/** 头像地址 */
	private String avatar;

	/** 状态：0-禁用，1-正常 */
	private Integer status;

	/** 部门ID */
	private Long deptId;

	/** 角色列表 */
	private List<String> roles;

	/** 权限标识列表 */
	private List<String> permissions;

	/** 创建时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date createTime;

	/** 更新时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date updateTime;
}
