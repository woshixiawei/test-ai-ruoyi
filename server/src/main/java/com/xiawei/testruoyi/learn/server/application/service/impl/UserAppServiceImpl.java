package com.xiawei.testruoyi.learn.server.application.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.application.service.UserAppService;
import com.xiawei.testruoyi.learn.server.application.bo.user.UserBO;
import com.xiawei.testruoyi.learn.server.application.bo.user.UserCreateBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.UserDO;
import com.xiawei.testruoyi.learn.server.domain.user.service.UserDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.UserConvertor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 用户应用服务实现
 *
 * <p>只通过 UserDomainService 访问数据层，不直接操作Repository。</p>
 * <p>Convertor调用集中在AppService层，Controller不直接操作Convertor。</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAppServiceImpl implements UserAppService {

	private final UserDomainService userDomainService;
	private final UserConvertor userConvertor;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long createUser(UserCreateBO bo) {
		// 1. 应用层参数校验（基本格式）
		validateCreate(bo);

		// 2. 校验用户名唯一（调用领域服务，领域层负责业务规则校验）
		userDomainService.validateUsernameNotExists(bo.getUsername());

		// 3. BO -> DO（MapStruct转换）
		UserDO user = userConvertor.toDomainFromCreateBO(bo);
		user.setStatus(1);
		// BCrypt 加密密码
		user.setPassword(BCrypt.hashpw(bo.getPassword()));

		// 4. 保存用户
		userDomainService.save(user);

		// 5. 分配角色
		if (!CollectionUtils.isEmpty(bo.getRoleIds())) {
			userDomainService.assignRoles(user.getId(), bo.getRoleIds());
		}

		log.info("[UserAppService] 用户创建成功, userId={}, username={}", user.getId(), bo.getUsername());
		return user.getId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUser(Long id, UserCreateBO bo) {
		// 1. 应用层参数校验
		validateUpdate(id, bo);

		// 2. 校验用户存在并获取当前数据（领域层负责存在性校验）
		UserDO user = userDomainService.validateUserExists(id);

		// 3. BO覆盖到DO（MapStruct @MappingTarget）
		userConvertor.updateDomainFromBO(bo, user);

		// 4. 保存更新
		userDomainService.save(user);

		// 5. 更新角色关联
		if (bo.getRoleIds() != null) {
			userDomainService.assignRoles(id, bo.getRoleIds());
		}

		log.info("[UserAppService] 用户更新成功, userId={}", id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteUser(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("用户ID不合法");
		}
		userDomainService.validateNotSuperAdmin(id);
		userDomainService.validateUserExists(id);
		userDomainService.deleteById(id);
		log.info("[UserAppService] 用户删除成功, userId={}", id);
	}

	@Override
	public UserBO getUserById(Long id) {
		UserDO user = userDomainService.getById(id);
		return userConvertor.toUserBO(user);
	}

	@Override
	public UserBO getUserByUsername(String username) {
		UserDO user = userDomainService.findByUsername(username);
		return userConvertor.toUserBO(user);
	}

	@Override
	public PageBO<UserBO> pageUsers(long current, long size, String keyword, Integer status) {
		PageDO<UserDO> doPage = userDomainService.findPage(current, size, keyword, status);
		return PageBO.from(doPage, userConvertor.toUserBOList(doPage.getRecords()));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void resetPassword(Long id, String newPassword) {
		if (id == null || id <= 0) {
			throw new ParamException("用户ID不合法");
		}
		if (newPassword == null || newPassword.isBlank()) {
			throw new ParamException("新密码不能为空");
		}
		userDomainService.validateNotSuperAdmin(id);
		UserDO user = userDomainService.validateUserExists(id);
		user.setPassword(BCrypt.hashpw(newPassword));
		userDomainService.save(user);
		log.info("[UserAppService] 用户密码重置成功, userId={}", id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateStatus(Long id, Integer status) {
		if (id == null || id <= 0) {
			throw new ParamException("用户ID不合法");
		}
		if (status == null || (status != 0 && status != 1)) {
			throw new ParamException("状态值不合法");
		}
		userDomainService.validateNotSuperAdmin(id);
		userDomainService.updateStatus(id, status);
		log.info("[UserAppService] 用户状态修改成功, userId={}, status={}", id, status);
	}

	/**
	 * 创建用户参数校验（应用层：基本格式校验）
	 */
	private void validateCreate(UserCreateBO bo) {
		if (bo == null) {
			throw new ParamException("创建参数不能为空");
		}
		if (bo.getUsername() == null || bo.getUsername().isBlank()) {
			throw new ParamException("用户名不能为空");
		}
		if (bo.getUsername().length() < 2 || bo.getUsername().length() > 20) {
			throw new ParamException("用户名长度必须在2-20个字符之间");
		}
		if (bo.getPassword() == null || bo.getPassword().isBlank()) {
			throw new ParamException("密码不能为空");
		}
	}

	/**
	 * 更新用户参数校验（应用层：基本格式校验）
	 */
	private void validateUpdate(Long id, UserCreateBO bo) {
		if (id == null || id <= 0) {
			throw new ParamException("用户ID不合法");
		}
		validateCreate(bo);
	}
}
