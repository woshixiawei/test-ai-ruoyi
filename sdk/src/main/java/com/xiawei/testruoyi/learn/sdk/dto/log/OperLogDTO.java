package com.xiawei.testruoyi.learn.sdk.dto.log;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 操作日志响应DTO
 */
@Data
@Schema(description = "操作日志信息")
public class OperLogDTO {

	@Schema(description = "日志ID")
	private Long id;

	@Schema(description = "操作模块标题")
	private String title;

	@Schema(description = "操作类型：1-新增，2-修改，3-删除，4-查询，5-其他")
	private Integer operType;

	@Schema(description = "请求方法名")
	private String method;

	@Schema(description = "请求方式")
	private String requestMethod;

	@Schema(description = "请求URL")
	private String operUrl;

	@Schema(description = "操作IP")
	private String operIp;

	@Schema(description = "操作地点")
	private String operLocation;

	@Schema(description = "请求参数")
	private String operParam;

	@Schema(description = "返回结果")
	private String jsonResult;

	@Schema(description = "状态：0-正常，1-异常")
	private Integer status;

	@Schema(description = "错误信息")
	private String errorMsg;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "操作时间")
	private Date operTime;

	@Schema(description = "执行耗时(ms)")
	private Long costTime;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "创建时间")
	private Date createTime;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "更新时间")
	private Date updateTime;
}
