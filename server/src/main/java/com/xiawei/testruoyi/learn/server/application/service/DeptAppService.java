package com.xiawei.testruoyi.learn.server.application.service;

import com.xiawei.testruoyi.learn.server.application.bo.dept.DeptBO;
import com.xiawei.testruoyi.learn.server.application.bo.dept.DeptCreateBO;

import java.util.List;

/**
 * 部门应用服务接口
 */
public interface DeptAppService {

	/** 新增部门 */
	Long createDept(DeptCreateBO bo);

	/** 修改部门 */
	void updateDept(Long id, DeptCreateBO bo);

	/** 删除部门 */
	void deleteDept(Long id);

	/** 部门详情 */
	DeptBO getDeptById(Long id);

	/** 部门列表 */
	List<DeptBO> listDepts();

	/** 部门树 */
	List<DeptBO> deptTree();
}
