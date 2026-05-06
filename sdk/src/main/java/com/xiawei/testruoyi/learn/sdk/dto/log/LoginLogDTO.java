package com.xiawei.testruoyi.learn.sdk.dto.log;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 登录日志响应DTO
 */
@Data
@Schema(description = "登录日志信息")
public class LoginLogDTO {

	@Schema(description = "日志ID")
	private Long id;

	@Schema(description = "用户名称")
	private String username;

	@Schema(description = "登录IP")
	private String ip;

	@Schema(description = "登录地点")
	private String location;

	@Schema(description = "浏览器")
	private String browser;

	@Schema(description = "操作系统")
	private String os;

	@Schema(description = "状态：0-成功，1-失败")
	private Integer status;

	@Schema(description = "提示消息")
	private String msg;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "登录时间")
	private Date loginTime;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "创建时间")
	private Date createTime;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "更新时间")
	private Date updateTime;
}
