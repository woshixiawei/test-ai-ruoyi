package com.xiawei.testruoyi.learn.server.application.service.impl;

import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.application.service.RoleAppService;
import com.xiawei.testruoyi.learn.server.application.bo.role.RoleBO;
import com.xiawei.testruoyi.learn.server.application.bo.role.RoleCreateBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.RoleDO;
import com.xiawei.testruoyi.learn.server.domain.user.service.RoleDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.RoleConvertor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色应用服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleAppServiceImpl implements RoleAppService {

	private final RoleDomainService roleDomainService;
	private final RoleConvertor roleConvertor;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long createRole(RoleCreateBO bo) {
		validateCreate(bo);
		roleDomainService.validateRoleCodeNotExists(bo.getRoleCode());
		RoleDO role = roleConvertor.toDomainFromCreateBO(bo);
		role.setStatus(1);
		roleDomainService.save(role);
		// 分配菜单
		if (bo.getMenuIds() != null && !bo.getMenuIds().isEmpty()) {
			roleDomainService.assignMenus(role.getId(), bo.getMenuIds());
		}
		log.info("[RoleAppService] 角色创建成功, roleId={}, roleCode={}", role.getId(), bo.getRoleCode());
		return role.getId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateRole(Long id, RoleCreateBO bo) {
		validateUpdate(id, bo);
		RoleDO role = roleDomainService.validateRoleExists(id);
		roleConvertor.updateDomainFromBO(bo, role);
		roleDomainService.save(role);
		// 更新菜单关联
		if (bo.getMenuIds() != null) {
			roleDomainService.assignMenus(id, bo.getMenuIds());
		}
		log.info("[RoleAppService] 角色更新成功, roleId={}", id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteRole(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("角色ID不合法");
		}
		roleDomainService.validateNotAdminRole(id);
		roleDomainService.validateRoleExists(id);
		roleDomainService.deleteById(id);
		log.info("[RoleAppService] 角色删除成功, roleId={}", id);
	}

	@Override
	public RoleBO getRoleById(Long id) {
		RoleDO role = roleDomainService.getById(id);
		return roleConvertor.toBO(role);
	}

	@Override
	public PageBO<RoleBO> pageRoles(long current, long size, String keyword, Integer status) {
		PageDO<RoleDO> doPage = roleDomainService.findPage(current, size, keyword, status);
		return PageBO.from(doPage, roleConvertor.toBOList(doPage.getRecords()));
	}

	@Override
	public List<Long> getRoleMenus(Long roleId) {
		if (roleId == null || roleId <= 0) {
			throw new ParamException("角色ID不合法");
		}
		return roleDomainService.getMenuIdsByRoleId(roleId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void assignRoleMenus(Long roleId, List<Long> menuIds) {
		if (roleId == null || roleId <= 0) {
			throw new ParamException("角色ID不合法");
		}
		roleDomainService.validateRoleExists(roleId);
		roleDomainService.assignMenus(roleId, menuIds);
		log.info("[RoleAppService] 角色菜单分配成功, roleId={}", roleId);
	}

	/**
	 * 创建角色参数校验（应用层：基本格式校验）
	 */
	private void validateCreate(RoleCreateBO bo) {
		if (bo == null) {
			throw new ParamException("创建参数不能为空");
		}
		if (bo.getRoleName() == null || bo.getRoleName().isBlank()) {
			throw new ParamException("角色名称不能为空");
		}
		if (bo.getRoleCode() == null || bo.getRoleCode().isBlank()) {
			throw new ParamException("角色编码不能为空");
		}
	}

	/**
	 * 更新角色参数校验（应用层：基本格式校验）
	 */
	private void validateUpdate(Long id, RoleCreateBO bo) {
		if (id == null || id <= 0) {
			throw new ParamException("角色ID不合法");
		}
		validateCreate(bo);
	}
}
