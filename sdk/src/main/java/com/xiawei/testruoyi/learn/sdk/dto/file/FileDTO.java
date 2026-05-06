package com.xiawei.testruoyi.learn.sdk.dto.file;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 文件响应DTO
 */
@Data
@Schema(description = "文件信息")
public class FileDTO {

	@Schema(description = "文件ID")
	private Long id;

	@Schema(description = "原文件名")
	private String fileName;

	@Schema(description = "存储路径")
	private String filePath;

	@Schema(description = "文件大小(bytes)")
	private Long fileSize;

	@Schema(description = "文件类型")
	private String fileType;

	@Schema(description = "访问URL")
	private String url;

	@Schema(description = "上传人ID")
	private Long createBy;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "创建时间")
	private Date createTime;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "更新时间")
	private Date updateTime;
}
