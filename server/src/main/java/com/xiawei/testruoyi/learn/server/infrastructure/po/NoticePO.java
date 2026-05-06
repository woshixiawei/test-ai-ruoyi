package com.xiawei.testruoyi.learn.server.infrastructure.po;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 通知公告持久化对象
 */
@Data
@Accessors(chain = true)
@TableName("sys_notice")
public class NoticePO extends Model<NoticePO> {

	/** 主键 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/** 公告标题 */
	private String title;

	/** 公告内容 */
	private String content;

	/** 类型：1-通知，2-公告 */
	private Integer noticeType;

	/** 状态：0-草稿，1-已发布，2-已撤回 */
	private Integer status;

	/** 发布人ID */
	private Long publisherId;

	/** 发布时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date publishTime;

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
