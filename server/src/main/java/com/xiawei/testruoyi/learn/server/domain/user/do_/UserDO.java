package com.xiawei.testruoyi.learn.server.domain.user.do_;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户领域数据对象（贫血模型）
 */
@Data
public class UserDO {

	/** 用户ID */
	private Long id;

	/** 用户名，唯一 */
	private String username;

	/** 用户昵称 */
	private String nickname;

	/** 邮箱 */
	private String email;

	/** 手机号 */
	private String phone;

	/** 头像地址 */
	private String avatar;

	/** 加密密码（BCrypt） */
	private String password;

	/** 状态：0-禁用，1-正常 */
	private Integer status;

	/** 所属部门ID */
	private Long deptId;

	/** 岗位ID */
	private Long postId;

	/** 备注 */
	private String remark;

	/** 角色ID列表 */
	private List<Long> roleIds;

	/** 创建时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date createTime;

	/** 更新时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date updateTime;
}
