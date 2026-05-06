package com.xiawei.testruoyi.learn.server.application.bo.file;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 文件业务对象
 */
@Data
public class FileBO {

	/** 主键 */
	private Long id;

	/** 原文件名 */
	private String fileName;

	/** 存储路径 */
	private String filePath;

	/** 文件大小(bytes) */
	private Long fileSize;

	/** 文件类型 */
	private String fileType;

	/** 访问URL */
	private String url;

	/** 上传人ID */
	private Long createBy;

	/** 创建时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date createTime;

	/** 更新时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date updateTime;
}
