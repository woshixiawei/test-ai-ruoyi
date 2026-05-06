package com.xiawei.testruoyi.learn.server.application.service;

import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.server.application.bo.log.LoginLogBO;
import com.xiawei.testruoyi.learn.server.application.bo.notice.NoticeBO;
import com.xiawei.testruoyi.learn.server.application.bo.user.ProfileBO;
import com.xiawei.testruoyi.learn.server.application.bo.user.UserCreateBO;

import java.util.List;

/**
 * 个人中心应用服务接口
 *
 * <p>入参和出参对象类型必须以 BO 结尾，禁止暴露Cmd/DTO等其他类型</p>
 */
public interface ProfileAppService {

	/** 获取个人信息 */
	ProfileBO getProfile();

	/** 修改个人信息 */
	void updateProfile(UserCreateBO bo);

	/** 修改密码（需验证旧密码） */
	void changePassword(String oldPassword, String newPassword);

	/** 个人登录历史 */
	PageBO<LoginLogBO> pageLoginLogs(long current, long size);

	/** 个人消息列表 */
	List<NoticeBO> listMessages();
}
