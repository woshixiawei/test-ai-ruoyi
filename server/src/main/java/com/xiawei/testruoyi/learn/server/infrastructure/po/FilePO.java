package com.xiawei.testruoyi.learn.server.infrastructure.po;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 文件管理持久化对象
 */
@Data
@Accessors(chain = true)
@TableName("sys_file")
public class FilePO {

	/** 主键 */
	@TableId(type = IdType.AUTO)
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

	/** 逻辑删除标志：0-未删除，非0-已删除（值为被删除记录的ID） */
	@TableLogic(value = "0", delval = "id")
	private Long isDeleted;

	/** 创建时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date createTime;

	/** 更新时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date updateTime;
}
