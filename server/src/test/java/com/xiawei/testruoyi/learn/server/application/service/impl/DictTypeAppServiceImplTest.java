package com.xiawei.testruoyi.learn.server.application.service.impl;

import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.server.application.bo.dict.DictTypeBO;
import com.xiawei.testruoyi.learn.server.application.bo.dict.DictTypeCreateBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.DictTypeDO;
import com.xiawei.testruoyi.learn.server.domain.user.service.DictTypeDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.DictTypeConvertor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 字典类型应用服务单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DictTypeAppService 单元测试")
class DictTypeAppServiceImplTest {

	@Mock
	private DictTypeDomainService dictTypeDomainService;

	@Mock
	private DictTypeConvertor dictTypeConvertor;

	@InjectMocks
	private DictTypeAppServiceImpl dictTypeAppService;

	private DictTypeCreateBO createBO;
	private DictTypeDO dictTypeDO;

	@BeforeEach
	void setUp() {
		createBO = new DictTypeCreateBO();
		createBO.setDictName("性别");
		createBO.setDictType("sys_user_sex");

		dictTypeDO = new DictTypeDO();
		dictTypeDO.setId(1L);
		dictTypeDO.setDictName("性别");
		dictTypeDO.setDictType("sys_user_sex");
	}

	@Nested
	@DisplayName("创建字典类型测试")
	class CreateDictTypeTests {

		@Test
		@DisplayName("参数为null时抛出ParamException")
		void createDictType_nullParam_throwsParamException() {
			assertThatThrownBy(() -> dictTypeAppService.createDictType(null))
					.isInstanceOf(ParamException.class)
					.hasMessage("创建参数不能为空");
		}

		@Test
		@DisplayName("字典名称为空时抛出ParamException")
		void createDictType_emptyName_throwsParamException() {
			createBO.setDictName("");
			assertThatThrownBy(() -> dictTypeAppService.createDictType(createBO))
					.isInstanceOf(ParamException.class)
					.hasMessage("字典名称不能为空");
		}

		@Test
		@DisplayName("字典类型编码为空时抛出ParamException")
		void createDictType_emptyCode_throwsParamException() {
			createBO.setDictType("");
			assertThatThrownBy(() -> dictTypeAppService.createDictType(createBO))
					.isInstanceOf(ParamException.class)
					.hasMessage("字典类型编码不能为空");
		}

		@Test
		@DisplayName("正常创建字典类型返回ID")
		void createDictType_validInput_returnsId() {
			when(dictTypeConvertor.toDomainFromCreateBO(createBO)).thenReturn(dictTypeDO);
			doAnswer(invocation -> {
				DictTypeDO dict = invocation.getArgument(0);
				dict.setId(1L);
				return null;
			}).when(dictTypeDomainService).save(any(DictTypeDO.class));

			Long id = dictTypeAppService.createDictType(createBO);
			assertThat(id).isEqualTo(1L);
			verify(dictTypeDomainService).validateDictTypeNotExists("sys_user_sex", null);
		}
	}

	@Nested
	@DisplayName("删除字典类型测试")
	class DeleteDictTypeTests {

		@Test
		@DisplayName("ID为null时抛出ParamException")
		void deleteDictType_nullId_throwsParamException() {
			assertThatThrownBy(() -> dictTypeAppService.deleteDictType(null))
					.isInstanceOf(ParamException.class)
					.hasMessage("字典类型ID不合法");
		}

		@Test
		@DisplayName("ID为0时抛出ParamException")
		void deleteDictType_zeroId_throwsParamException() {
			assertThatThrownBy(() -> dictTypeAppService.deleteDictType(0L))
					.isInstanceOf(ParamException.class)
					.hasMessage("字典类型ID不合法");
		}
	}

	@Nested
	@DisplayName("查询字典类型测试")
	class QueryDictTypeTests {

		@Test
		@DisplayName("ID为null时抛出ParamException")
		void getDictTypeById_nullId_throwsParamException() {
			assertThatThrownBy(() -> dictTypeAppService.getDictTypeById(null))
					.isInstanceOf(ParamException.class)
					.hasMessage("字典类型ID不合法");
		}

		@Test
		@DisplayName("ID为负数时抛出ParamException")
		void getDictTypeById_negativeId_throwsParamException() {
			assertThatThrownBy(() -> dictTypeAppService.getDictTypeById(-1L))
					.isInstanceOf(ParamException.class)
					.hasMessage("字典类型ID不合法");
		}
	}
}
