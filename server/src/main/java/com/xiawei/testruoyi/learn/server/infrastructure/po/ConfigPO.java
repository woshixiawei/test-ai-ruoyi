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
 * 参数配置持久化对象
 */
@Data
@Accessors(chain = true)
@TableName("sys_config")
public class ConfigPO extends Model<ConfigPO> {

	/** 主键 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/** 参数名称 */
	private String configName;

	/** 参数键名，唯一 */
	private String configKey;

	/** 参数键值 */
	private String configValue;

	/** 是否系统内置：0-否，1-是 */
	private Integer isSystem;

	/** 备注 */
	private String remark;

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
