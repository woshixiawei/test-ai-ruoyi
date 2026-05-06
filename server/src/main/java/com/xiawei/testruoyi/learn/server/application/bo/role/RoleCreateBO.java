package com.xiawei.testruoyi.learn.server.application.bo.role;

import lombok.Data;

import java.util.List;

/**
 * 角色创建/修改业务对象
 */
@Data
public class RoleCreateBO {

	/** 角色名称 */
	private String roleName;

	/** 角色编码，唯一 */
	private String roleCode;

	/** 显示顺序 */
	private Integer sort;

	/** 数据范围 */
	private Integer dataScope;

	/** 状态 */
	private Integer status;

	/** 备注 */
	private String remark;

	/** 已分配菜单ID列表 */
	private List<Long> menuIds;
}
