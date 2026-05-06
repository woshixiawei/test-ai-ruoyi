package com.xiawei.testruoyi.learn.server.application.bo.post;

import lombok.Data;

/**
 * 岗位创建/修改业务对象
 */
@Data
public class PostCreateBO {

	/** 岗位编码 */
	private String postCode;

	/** 岗位名称 */
	private String postName;

	/** 显示顺序 */
	private Integer sort;

	/** 状态 */
	private Integer status;

	/** 备注 */
	private String remark;
}
