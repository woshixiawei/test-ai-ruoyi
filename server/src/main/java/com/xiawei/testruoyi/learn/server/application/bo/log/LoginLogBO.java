package com.xiawei.testruoyi.learn.server.application.bo.log;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 登录日志业务对象
 */
@Data
public class LoginLogBO {

	/** 主键 */
	private Long id;

	/** 用户名称 */
	private String username;

	/** 登录IP */
	private String ip;

	/** 登录地点 */
	private String location;

	/** 浏览器 */
	private String browser;

	/** 操作系统 */
	private String os;

	/** 状态：0-成功，1-失败 */
	private Integer status;

	/** 提示消息 */
	private String msg;

	/** 登录时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date loginTime;

	/** 创建时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date createTime;

	/** 更新时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date updateTime;
}
