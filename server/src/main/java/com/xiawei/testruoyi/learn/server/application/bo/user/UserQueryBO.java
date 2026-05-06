package com.xiawei.testruoyi.learn.server.application.bo.user;

import lombok.Data;

/**
 * 用户查询业务对象
 */
@Data
public class UserQueryBO {

	/** 用户名 */
	private String username;

	/** 状态 */
	private Integer status;

	/** 部门ID */
	private Long deptId;
}
