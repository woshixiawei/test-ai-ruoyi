package com.xiawei.testruoyi.learn.server.application.bo.dept;

import lombok.Data;

/**
 * 部门创建/修改业务对象
 */
@Data
public class DeptCreateBO {

	/** 部门名称 */
	private String deptName;

	/** 父部门ID */
	private Long parentId;

	/** 显示顺序 */
	private Integer sort;

	/** 负责人 */
	private String leader;

	/** 联系电话 */
	private String phone;

	/** 邮箱 */
	private String email;

	/** 状态 */
	private Integer status;
}
