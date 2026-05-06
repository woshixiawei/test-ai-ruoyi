package com.xiawei.testruoyi.learn.server.infrastructure.query;

import lombok.Data;

/**
 * 用户查询参数对象
 *
 * <p>当查询方法入参超过4个时，必须封装为Query对象。</p>
 */
@Data
public class UserQuery {

	/** 当前页码 */
	private long current = 1;

	/** 每页大小 */
	private long size = 10;

	/** 搜索关键词（用户名/昵称/邮箱/手机号模糊匹配） */
	private String keyword;

	/** 状态筛选：0-禁用，1-正常 */
	private Integer status;
}
