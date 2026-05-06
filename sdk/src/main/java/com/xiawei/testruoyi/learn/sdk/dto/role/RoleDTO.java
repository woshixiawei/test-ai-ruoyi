package com.xiawei.testruoyi.learn.sdk.dto.role;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 角色响应DTO
 */
@Data
@Schema(description = "角色信息")
public class RoleDTO {

	@Schema(description = "角色ID")
	private Long id;

	@Schema(description = "角色名称")
	private String roleName;

	@Schema(description = "角色编码")
	private String roleCode;

	@Schema(description = "显示顺序")
	private Integer sort;

	@Schema(description = "状态：0-禁用，1-正常")
	private Integer status;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "已分配菜单ID列表")
	private List<Long> menuIds;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "创建时间")
	private Date createTime;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "更新时间")
	private Date updateTime;
}
