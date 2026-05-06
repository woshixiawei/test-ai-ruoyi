package com.xiawei.testruoyi.learn.server.application.service.impl;

import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.server.application.service.DeptAppService;
import com.xiawei.testruoyi.learn.server.application.bo.dept.DeptBO;
import com.xiawei.testruoyi.learn.server.application.bo.dept.DeptCreateBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.DeptDO;
import com.xiawei.testruoyi.learn.server.domain.user.service.DeptDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.DeptConvertor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 部门应用服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeptAppServiceImpl implements DeptAppService {

	private final DeptDomainService deptDomainService;
	private final DeptConvertor deptConvertor;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long createDept(DeptCreateBO bo) {
		validateCreate(bo);
		DeptDO dept = deptConvertor.toDomainFromCreateBO(bo);
		if (dept.getStatus() == null) {
			dept.setStatus(1);
		}
		deptDomainService.save(dept);
		log.info("[DeptAppService] 部门创建成功, deptId={}", dept.getId());
		return dept.getId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateDept(Long id, DeptCreateBO bo) {
		validateUpdate(id, bo);
		DeptDO dept = deptDomainService.validateDeptExists(id);
		// 循环依赖防护
		deptDomainService.validateParentNotChild(id, bo.getParentId());
		// 使用MapStruct更新DO（禁止手动setter）
		deptConvertor.updateDomainFromBO(bo, dept);
		deptDomainService.save(dept);
		log.info("[DeptAppService] 部门更新成功, deptId={}", id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteDept(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("部门ID不合法");
		}
		deptDomainService.validateNoUsers(id);
		deptDomainService.deleteById(id);
		log.info("[DeptAppService] 部门删除成功, deptId={}", id);
	}

	@Override
	public DeptBO getDeptById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("部门ID不合法");
		}
		return deptConvertor.toBO(deptDomainService.getById(id));
	}

	@Override
	public List<DeptBO> listDepts() {
		List<DeptDO> depts = deptDomainService.findAll();
		return deptConvertor.toBOList(depts);
	}

	@Override
	public List<DeptBO> deptTree() {
		List<DeptDO> depts = deptDomainService.findAll();
		List<DeptDO> tree = deptDomainService.buildTree(depts);
		return deptConvertor.toBOList(tree);
	}

	/**
	 * 创建部门参数校验（应用层：基本格式校验）
	 */
	private void validateCreate(DeptCreateBO bo) {
		if (bo == null) {
			throw new ParamException("创建参数不能为空");
		}
		if (bo.getDeptName() == null || bo.getDeptName().isBlank()) {
			throw new ParamException("部门名称不能为空");
		}
		if (bo.getParentId() == null || bo.getParentId() < 0) {
			throw new ParamException("父部门ID不合法");
		}
	}

	/**
	 * 更新部门参数校验（应用层：基本格式校验）
	 */
	private void validateUpdate(Long id, DeptCreateBO bo) {
		if (id == null || id <= 0) {
			throw new ParamException("部门ID不合法");
		}
		validateCreate(bo);
	}
}
