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
 * 字典数据持久化对象
 */
@Data
@Accessors(chain = true)
@TableName("sys_dict_data")
public class DictDataPO extends Model<DictDataPO> {

	/** 主键 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/** 字典类型ID */
	private Long dictTypeId;

	/** 数据标签 */
	private String dictLabel;

	/** 数据键值 */
	private String dictValue;

	/** 显示顺序 */
	private Integer sort;

	/** 状态：0-禁用，1-正常 */
	private Integer status;

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
