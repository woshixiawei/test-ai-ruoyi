package com.xiawei.testruoyi.learn.server.application.bo.user;

import lombok.Data;

/**
 * 用户创建/修改业务对象
 */
@Data
public class UserCreateBO {

	/** 用户名 */
	private String username;

	/** 昵称 */
	private String nickname;

	/** 密码 */
	private String password;

	/** 邮箱 */
	private String email;

	/** 手机号 */
	private String phone;

	/** 性别 */
	private Integer sex;

	/** 状态 */
	private Integer status;

	/** 部门ID */
	private Long deptId;

	/** 角色ID列表 */
	private java.util.List<Long> roleIds;

	/** 岗位ID列表 */
	private java.util.List<Long> postIds;

	/** 备注 */
	private String remark;
}
