package com.xiawei.testruoyi.learn.server.domain.user.service;

import com.xiawei.testruoyi.learn.base.exceptions.BusinessException;
import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.server.domain.user.do_.DeptDO;
import com.xiawei.testruoyi.learn.server.domain.user.repository.DeptRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.DeptConvertor;
import com.xiawei.testruoyi.learn.server.infrastructure.po.DeptPO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门领域服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeptDomainService {

	private final DeptRepository deptRepository;
	private final DeptConvertor deptConvertor;

	/** 保存部门 */
	public void save(DeptDO dept) {
		DeptPO po = deptConvertor.toPO(dept);
		deptRepository.saveOrUpdate(po);
		dept.setId(po.getId());
	}

	/** 根据ID查询部门 */
	public DeptDO getById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("部门ID不合法");
		}
		DeptPO po = deptRepository.getById(id);
		if (po == null) {
			throw new BusinessException("部门不存在");
		}
		return deptConvertor.toDomain(po);
	}

	/** 校验部门存在（数据存在性校验） */
	public DeptDO validateDeptExists(Long deptId) {
		if (deptId == null || deptId <= 0) {
			throw new ParamException("部门ID不合法");
		}
		DeptPO po = deptRepository.getById(deptId);
		if (po == null) {
			throw new BusinessException("部门不存在");
		}
		return deptConvertor.toDomain(po);
	}

	/** 校验部门下无用户（业务规则校验：删除前校验） */
	public void validateNoUsers(Long deptId) {
		if (deptId == null || deptId <= 0) {
			throw new ParamException("部门ID不合法");
		}
		if (deptRepository.existsUserByDeptId(deptId)) {
			throw new BusinessException("该部门下存在用户，禁止删除");
		}
	}

	/** 校验父部门不是自身或子部门（业务规则校验：循环依赖防护） */
	public void validateParentNotChild(Long deptId, Long newParentId) {
		if (deptId == null || deptId <= 0) {
			throw new ParamException("部门ID不合法");
		}
		if (newParentId == null || newParentId < 0) {
			throw new ParamException("父部门ID不合法");
		}
		if (deptId.equals(newParentId)) {
			throw new BusinessException("父部门不能是自身");
		}
		List<Long> childIds = deptRepository.findChildDeptIds(deptId);
		if (childIds.contains(newParentId)) {
			throw new BusinessException("父部门不能是自身的子部门");
		}
	}

	/** 删除部门 */
	public void deleteById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("部门ID不合法");
		}
		deptRepository.removeById(id);
	}

	/** 查询所有部门列表 */
	public List<DeptDO> findAll() {
		List<DeptPO> pos = deptRepository.findAllOrder();
		return deptConvertor.toDomainList(pos);
	}

	/** 构建部门树 */
	public List<DeptDO> buildTree(List<DeptDO> depts) {
		Map<Long, List<DeptDO>> groupByParent = depts.stream()
				.collect(Collectors.groupingBy(DeptDO::getParentId));
		depts.forEach(d -> d.setChildren(groupByParent.getOrDefault(d.getId(), new ArrayList<>())));
		return depts.stream().filter(d -> d.getParentId() == 0L).collect(Collectors.toList());
	}
}
