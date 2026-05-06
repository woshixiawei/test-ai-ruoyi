package com.xiawei.testruoyi.learn.sdk.dto.dept;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 部门响应DTO
 */
@Data
@Schema(description = "部门信息")
public class DeptDTO {

	@Schema(description = "部门ID")
	private Long id;

	@Schema(description = "部门名称")
	private String deptName;

	@Schema(description = "父部门ID")
	private Long parentId;

	@Schema(description = "显示顺序")
	private Integer sort;

	@Schema(description = "负责人")
	private String leader;

	@Schema(description = "联系电话")
	private String phone;

	@Schema(description = "邮箱")
	private String email;

	@Schema(description = "状态")
	private Integer status;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "创建时间")
	private Date createTime;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "更新时间")
	private Date updateTime;

	@Schema(description = "子部门列表")
	private List<DeptDTO> children;
}
