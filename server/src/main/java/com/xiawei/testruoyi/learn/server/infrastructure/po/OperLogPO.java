package com.xiawei.testruoyi.learn.server.infrastructure.po;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 操作日志持久化对象
 */
@Data
@Accessors(chain = true)
@TableName("sys_oper_log")
public class OperLogPO {

	/** 主键 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/** 操作模块标题 */
	private String title;

	/** 操作类型：1-新增，2-修改，3-删除，4-查询，5-其他 */
	private Integer operType;

	/** 请求方法名 */
	private String method;

	/** 请求方式：GET/POST/PUT/DELETE */
	private String requestMethod;

	/** 请求URL */
	private String operUrl;

	/** 操作IP */
	private String operIp;

	/** 操作地点 */
	private String operLocation;

	/** 请求参数 */
	private String operParam;

	/** 返回结果 */
	private String jsonResult;

	/** 状态：0-正常，1-异常 */
	private Integer status;

	/** 错误信息 */
	private String errorMsg;

	/** 操作时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date operTime;

	/** 执行耗时(ms) */
	private Long costTime;

	/** 创建时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date createTime;

	/** 更新时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date updateTime;
}
