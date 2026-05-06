package com.xiawei.testruoyi.learn.server.application.bo.role;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 角色业务对象
 */
@Data
public class RoleBO {

	/** 主键 */
	private Long id;

	/** 角色名称 */
	private String roleName;

	/** 角色编码，唯一 */
	private String roleCode;

	/** 显示顺序 */
	private Integer sort;

	/** 数据范围：1-全部数据，2-自定义，3-本部门，4-本部门及以下，5-仅本人 */
	private Integer dataScope;

	/** 状态：0-禁用，1-正常 */
	private Integer status;

	/** 备注 */
	private String remark;

	/** 创建时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date createTime;

	/** 更新时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date updateTime;
}
