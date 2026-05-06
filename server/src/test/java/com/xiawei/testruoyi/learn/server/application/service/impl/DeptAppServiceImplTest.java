package com.xiawei.testruoyi.learn.server.application.service.impl;

import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.server.application.bo.dept.DeptBO;
import com.xiawei.testruoyi.learn.server.application.bo.dept.DeptCreateBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.DeptDO;
import com.xiawei.testruoyi.learn.server.domain.user.service.DeptDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.DeptConvertor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 部门应用服务单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DeptAppService 单元测试")
class DeptAppServiceImplTest {

	@Mock
	private DeptDomainService deptDomainService;

	@Mock
	private DeptConvertor deptConvertor;

	@InjectMocks
	private DeptAppServiceImpl deptAppService;

	private DeptCreateBO createBO;
	private DeptDO deptDO;
	private DeptBO deptBO;

	@BeforeEach
	void setUp() {
		createBO = new DeptCreateBO();
		createBO.setDeptName("技术部");
		createBO.setParentId(0L);

		deptDO = new DeptDO();
		deptDO.setId(1L);
		deptDO.setDeptName("技术部");

		deptBO = new DeptBO();
		deptBO.setId(1L);
		deptBO.setDeptName("技术部");
	}

	@Nested
	@DisplayName("创建部门测试")
	class CreateDeptTests {

		@Test
		@DisplayName("参数为null时抛出ParamException")
		void createDept_nullParam_throwsParamException() {
			assertThatThrownBy(() -> deptAppService.createDept(null))
					.isInstanceOf(ParamException.class)
					.hasMessage("创建参数不能为空");
		}

		@Test
		@DisplayName("部门名称为空时抛出ParamException")
		void createDept_emptyName_throwsParamException() {
			createBO.setDeptName("");
			assertThatThrownBy(() -> deptAppService.createDept(createBO))
					.isInstanceOf(ParamException.class)
					.hasMessage("部门名称不能为空");
		}

		@Test
		@DisplayName("父部门ID为null时抛出ParamException")
		void createDept_nullParentId_throwsParamException() {
			createBO.setParentId(null);
			assertThatThrownBy(() -> deptAppService.createDept(createBO))
					.isInstanceOf(ParamException.class)
					.hasMessage("父部门ID不合法");
		}

		@Test
		@DisplayName("父部门ID为负数时抛出ParamException")
		void createDept_negativeParentId_throwsParamException() {
			createBO.setParentId(-1L);
			assertThatThrownBy(() -> deptAppService.createDept(createBO))
					.isInstanceOf(ParamException.class)
					.hasMessage("父部门ID不合法");
		}

		@Test
		@DisplayName("正常创建部门返回部门ID")
		void createDept_validInput_returnsDeptId() {
			when(deptConvertor.toDomainFromCreateBO(createBO)).thenReturn(deptDO);
			doAnswer(invocation -> {
				DeptDO dept = invocation.getArgument(0);
				dept.setId(1L);
				return null;
			}).when(deptDomainService).save(any(DeptDO.class));

			Long id = deptAppService.createDept(createBO);
			assertThat(id).isEqualTo(1L);
		}
	}

	@Nested
	@DisplayName("更新部门测试")
	class UpdateDeptTests {

		@Test
		@DisplayName("ID为null时抛出ParamException")
		void updateDept_nullId_throwsParamException() {
			assertThatThrownBy(() -> deptAppService.updateDept(null, createBO))
					.isInstanceOf(ParamException.class)
					.hasMessage("部门ID不合法");
		}

		@Test
		@DisplayName("循环依赖校验由DomainService处理")
		void updateDept_circularDep_domainServiceValidates() {
			when(deptDomainService.validateDeptExists(1L)).thenReturn(deptDO);
			doThrow(new ParamException("父部门不能是自身或自身的子部门"))
					.when(deptDomainService).validateParentNotChild(1L, 0L);

			assertThatThrownBy(() -> deptAppService.updateDept(1L, createBO))
					.isInstanceOf(ParamException.class)
					.hasMessage("父部门不能是自身或自身的子部门");
		}
	}

	@Nested
	@DisplayName("删除部门测试")
	class DeleteDeptTests {

		@Test
		@DisplayName("ID为null时抛出ParamException")
		void deleteDept_nullId_throwsParamException() {
			assertThatThrownBy(() -> deptAppService.deleteDept(null))
					.isInstanceOf(ParamException.class)
					.hasMessage("部门ID不合法");
		}

		@Test
		@DisplayName("部门下有用户时由DomainService校验")
		void deleteDept_hasUsers_domainServiceValidates() {
			doThrow(new ParamException("部门下存在用户，无法删除"))
					.when(deptDomainService).validateNoUsers(1L);

			assertThatThrownBy(() -> deptAppService.deleteDept(1L))
					.isInstanceOf(ParamException.class)
					.hasMessage("部门下存在用户，无法删除");
		}
	}
}
