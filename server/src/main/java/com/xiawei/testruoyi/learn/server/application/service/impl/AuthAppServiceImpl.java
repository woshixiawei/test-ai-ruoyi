package com.xiawei.testruoyi.learn.server.application.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.xiawei.testruoyi.learn.base.exceptions.BusinessException;
import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.server.application.service.AuthAppService;
import com.xiawei.testruoyi.learn.server.application.bo.auth.LoginBO;
import com.xiawei.testruoyi.learn.server.application.bo.auth.LoginResultBO;
import com.xiawei.testruoyi.learn.server.application.bo.menu.MenuBO;
import com.xiawei.testruoyi.learn.server.application.bo.user.UserBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.MenuDO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.RoleDO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.UserDO;
import com.xiawei.testruoyi.learn.server.domain.user.service.MenuDomainService;
import com.xiawei.testruoyi.learn.server.domain.user.service.RoleDomainService;
import com.xiawei.testruoyi.learn.server.domain.user.service.UserDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.MenuConvertor;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.UserConvertor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证应用服务实现
 *
 * <p>直接注入 UserDomainService 进行跨域调用（共享层级架构下 AppService 可直接访问所有DomainService）。</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthAppServiceImpl implements AuthAppService {

	private final UserDomainService userDomainService;
	private final RoleDomainService roleDomainService;
	private final MenuDomainService menuDomainService;
	private final UserConvertor userConvertor;
	private final MenuConvertor menuConvertor;

	@Override
	public LoginResultBO login(LoginBO bo) {
		// 1. 应用层参数校验
		if (bo == null) {
			throw new ParamException("登录参数不能为空");
		}
		if (bo.getUsername() == null || bo.getUsername().isBlank()) {
			throw new ParamException("用户名不能为空");
		}
		if (bo.getPassword() == null || bo.getPassword().isBlank()) {
			throw new ParamException("密码不能为空");
		}

		// 2. 直接调用用户领域服务查询用户
		UserDO user = userDomainService.findByUsername(bo.getUsername());

		// 3. 校验用户状态
		if (user.getStatus() == 0) {
			throw new BusinessException("账号已停用");
		}

		// 4. 校验密码（BCrypt）
		if (!BCrypt.checkpw(bo.getPassword(), user.getPassword())) {
			throw new BusinessException("用户名或密码错误");
		}

		// 5. 登录并生成Token
		StpUtil.login(user.getId());

		// 6. 查询用户角色和权限
		List<RoleDO> roles = roleDomainService.findByUserId(user.getId());
		List<String> roleKeys = roles.stream().map(RoleDO::getRoleCode).collect(Collectors.toList());
		List<String> permissions;
		if (user.getId() == 1L) {
			// 超级管理员拥有所有权限
			permissions = Collections.singletonList("*");
			if (roleKeys.isEmpty()) {
				roleKeys = Collections.singletonList("admin");
			}
		} else {
			List<Long> roleIds = roles.stream().map(RoleDO::getId).collect(Collectors.toList());
			List<MenuDO> menus = menuDomainService.findByRoleIds(roleIds);
			permissions = menus.stream()
					.filter(m -> m.getPermission() != null && !m.getPermission().isEmpty())
					.map(MenuDO::getPermission)
					.distinct()
					.collect(Collectors.toList());
		}

		// 7. 构建返回 BO
		LoginResultBO result = new LoginResultBO();
		result.setToken(StpUtil.getTokenValue());
		result.setUserInfo(userConvertor.toUserBO(user));
		result.setRoles(roleKeys);
		result.setPermissions(permissions);

		log.info("[AuthAppService] 用户登录成功, userId={}, username={}", user.getId(), bo.getUsername());
		return result;
	}

	@Override
	public void logout() {
		StpUtil.logout();
		log.info("[AuthAppService] 用户登出成功");
	}

	@Override
	public UserBO getCurrentUserInfo() {
		Long userId = StpUtil.getLoginIdAsLong();
		UserDO user = userDomainService.getById(userId);
		return userConvertor.toUserBO(user);
	}

	@Override
	public List<MenuBO> getCurrentUserRoutes() {
		Long userId = StpUtil.getLoginIdAsLong();
		if (userId == null || userId <= 0) {
			throw new ParamException("用户ID不合法");
		}
		if (userId == 1L) {
			List<MenuDO> allMenus = menuDomainService.findAll();
			List<MenuDO> tree = menuDomainService.buildTree(allMenus);
			return menuConvertor.toBOList(tree);
		}
		List<RoleDO> roles = roleDomainService.findByUserId(userId);
		List<Long> roleIds = roles.stream().map(RoleDO::getId).collect(Collectors.toList());
		List<MenuDO> menus = menuDomainService.findByRoleIds(roleIds);
		List<MenuDO> tree = menuDomainService.buildTree(menus);
		return menuConvertor.toBOList(tree);
	}

	@Override
	public List<String> getCurrentUserPermissions() {
		Long userId = StpUtil.getLoginIdAsLong();
		if (userId == 1L) {
			return Collections.singletonList("*");
		}
		List<RoleDO> roles = roleDomainService.findByUserId(userId);
		List<Long> roleIds = roles.stream().map(RoleDO::getId).collect(Collectors.toList());
		List<MenuDO> menus = menuDomainService.findByRoleIds(roleIds);
		return menus.stream()
				.filter(m -> m.getPermission() != null && !m.getPermission().isEmpty())
				.map(MenuDO::getPermission)
				.distinct()
				.collect(Collectors.toList());
	}

	@Override
	public List<String> getCurrentUserRoles() {
		Long userId = StpUtil.getLoginIdAsLong();
		List<RoleDO> roles = roleDomainService.findByUserId(userId);
		List<String> roleKeys = roles.stream().map(RoleDO::getRoleCode).collect(Collectors.toList());
		if (userId == 1L && roleKeys.isEmpty()) {
			return Collections.singletonList("admin");
		}
		return roleKeys;
	}
}
