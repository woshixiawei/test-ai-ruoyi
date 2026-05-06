package com.xiawei.testruoyi.learn.server.domain.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.exceptions.BusinessException;
import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.RoleDO;
import com.xiawei.testruoyi.learn.server.domain.user.repository.RoleRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.RoleConvertor;
import com.xiawei.testruoyi.learn.server.infrastructure.po.RolePO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色领域服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleDomainService {

	private final RoleRepository roleRepository;
	private final RoleConvertor roleConvertor;

	/**
	 * 校验角色编码唯一性（业务规则校验）
	 */
	public void validateRoleCodeNotExists(String roleCode) {
		if (roleCode == null || roleCode.isBlank()) {
			throw new ParamException("角色编码不能为空");
		}
		RolePO po = roleRepository.findByRoleCode(roleCode);
		if (po != null) {
			throw new BusinessException("角色编码已存在");
		}
	}

	/**
	 * 校验角色是否存在（数据存在性校验）
	 */
	public RoleDO validateRoleExists(Long roleId) {
		if (roleId == null || roleId <= 0) {
			throw new ParamException("角色ID不合法");
		}
		RolePO po = roleRepository.getById(roleId);
		if (po == null) {
			throw new BusinessException("角色不存在");
		}
		return toDO(po);
	}

	/**
	 * 校验角色非超级管理员角色（业务规则校验）
	 */
	public void validateNotAdminRole(Long roleId) {
		if (roleId == null || roleId <= 0) {
			throw new ParamException("角色ID不合法");
		}
		RolePO po = roleRepository.getById(roleId);
		if (po != null && "admin".equals(po.getRoleCode())) {
			throw new BusinessException("超级管理员角色禁止操作");
		}
	}

	/**
	 * 保存角色
	 */
	public void save(RoleDO role) {
		RolePO po = roleConvertor.toPO(role);
		roleRepository.saveOrUpdate(po);
		role.setId(po.getId());
	}

	/**
	 * 根据ID查询角色
	 */
	public RoleDO getById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("角色ID不合法");
		}
		RolePO po = roleRepository.getById(id);
		if (po == null) {
			throw new BusinessException("角色不存在");
		}
		return toDO(po);
	}

	/**
	 * 删除角色
	 */
	public void deleteById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("角色ID不合法");
		}
		roleRepository.removeById(id);
	}

	/**
	 * 分页查询角色
	 */
	public PageDO<RoleDO> findPage(long current, long size, String keyword, Integer status) {
		Page<RolePO> poPage = roleRepository.findPage(current, size, keyword, status);
		return PageDO.from(poPage, roleConvertor.toDomainList(poPage.getRecords()));
	}

	/**
	 * 查询角色已分配菜单ID列表
	 */
	public List<Long> getMenuIdsByRoleId(Long roleId) {
		return roleRepository.findMenuIdsByRoleId(roleId);
	}

	/**
	 * 分配角色菜单
	 */
	public void assignMenus(Long roleId, List<Long> menuIds) {
		if (roleId == null || roleId <= 0) {
			throw new ParamException("角色ID不合法");
		}
		roleRepository.assignMenus(roleId, menuIds);
	}

	/**
	 * 根据用户ID查询角色列表
	 */
	public List<RoleDO> findByUserId(Long userId) {
		if (userId == null || userId <= 0) {
			throw new ParamException("用户ID不合法");
		}
		List<RolePO> pos = roleRepository.findByUserId(userId);
		return roleConvertor.toDomainList(pos);
	}

	/**
	 * PO → DO 转换（含菜单ID查询）
	 */
	private RoleDO toDO(RolePO po) {
		RoleDO role = roleConvertor.toDomain(po);
		List<Long> menuIds = roleRepository.findMenuIdsByRoleId(po.getId());
		role.setMenuIds(menuIds);
		return role;
	}
}
