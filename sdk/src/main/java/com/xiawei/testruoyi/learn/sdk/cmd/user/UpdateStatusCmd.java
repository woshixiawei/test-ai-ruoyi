package com.xiawei.testruoyi.learn.sdk.cmd.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 修改状态命令
 */
@Data
@Schema(description = "修改状态请求")
public class UpdateStatusCmd {

	@NotNull(message = "状态不能为空")
	@Schema(description = "状态：0-禁用，1-正常")
	private Integer status;
}
