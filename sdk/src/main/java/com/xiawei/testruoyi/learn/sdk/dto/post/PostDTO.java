package com.xiawei.testruoyi.learn.sdk.dto.post;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 岗位响应DTO
 */
@Data
@Schema(description = "岗位信息")
public class PostDTO {

	@Schema(description = "岗位ID")
	private Long id;

	@Schema(description = "岗位编码")
	private String postCode;

	@Schema(description = "岗位名称")
	private String postName;

	@Schema(description = "显示顺序")
	private Integer sort;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "备注")
	private String remark;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "创建时间")
	private Date createTime;

	@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	@Schema(description = "更新时间")
	private Date updateTime;
}
