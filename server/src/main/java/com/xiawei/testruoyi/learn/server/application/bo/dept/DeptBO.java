package com.xiawei.testruoyi.learn.server.application.bo.dept;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 部门业务对象
 */
@Data
public class DeptBO {

	/** 主键 */
	private Long id;

	/** 部门名称 */
	private String deptName;

	/** 父部门ID */
	private Long parentId;

	/** 显示顺序 */
	private Integer sort;

	/** 负责人 */
	private String leader;

	/** 联系电话 */
	private String phone;

	/** 邮箱 */
	private String email;

	/** 状态 */
	private Integer status;

	/** 子部门列表 */
	private List<DeptBO> children;

	/** 创建时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date createTime;

	/** 更新时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date updateTime;
}
