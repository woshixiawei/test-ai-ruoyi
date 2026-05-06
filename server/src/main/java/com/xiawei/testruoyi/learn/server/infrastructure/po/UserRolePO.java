package com.xiawei.testruoyi.learn.server.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户角色关联持久化对象
 *
 * <p>对应 sys_user_role 关系表，用于纯 Java 链式查询替代 XML 映射。</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user_role")
public class UserRolePO {

	/** 用户ID */
	private Long userId;

	/** 角色ID */
	private Long roleId;
}
