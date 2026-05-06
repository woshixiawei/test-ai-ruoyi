package com.xiawei.testruoyi.learn.server.domain.user.do_;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 部门领域数据对象
 */
@Data
public class DeptDO {

	/** 主键 */
	private Long id;

	/** 部门名称 */
	private String deptName;

	/** 父部门ID，0为顶级 */
	private Long parentId;

	/** 显示顺序 */
	private Integer sort;

	/** 负责人 */
	private String leader;

	/** 联系电话 */
	private String phone;

	/** 邮箱 */
	private String email;

	/** 状态：0-禁用，1-正常 */
	private Integer status;

	/** 子部门列表 */
	private List<DeptDO> children;

	/** 创建时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date createTime;

	/** 更新时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date updateTime;
}
