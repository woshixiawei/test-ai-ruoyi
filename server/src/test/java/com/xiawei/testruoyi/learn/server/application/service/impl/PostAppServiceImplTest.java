package com.xiawei.testruoyi.learn.server.application.service.impl;

import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.server.application.bo.post.PostBO;
import com.xiawei.testruoyi.learn.server.application.bo.post.PostCreateBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.PostDO;
import com.xiawei.testruoyi.learn.server.domain.user.service.PostDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.PostConvertor;
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
 * 岗位应用服务单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PostAppService 单元测试")
class PostAppServiceImplTest {

	@Mock
	private PostDomainService postDomainService;

	@Mock
	private PostConvertor postConvertor;

	@InjectMocks
	private PostAppServiceImpl postAppService;

	private PostCreateBO createBO;
	private PostDO postDO;

	@BeforeEach
	void setUp() {
		createBO = new PostCreateBO();
		createBO.setPostCode("CEO");
		createBO.setPostName("董事长");

		postDO = new PostDO();
		postDO.setId(1L);
		postDO.setPostCode("CEO");
		postDO.setPostName("董事长");
	}

	@Nested
	@DisplayName("创建岗位测试")
	class CreatePostTests {

		@Test
		@DisplayName("参数为null时抛出ParamException")
		void createPost_nullParam_throwsParamException() {
			assertThatThrownBy(() -> postAppService.createPost(null))
					.isInstanceOf(ParamException.class)
					.hasMessage("创建参数不能为空");
		}

		@Test
		@DisplayName("岗位编码为空时抛出ParamException")
		void createPost_emptyCode_throwsParamException() {
			createBO.setPostCode("");
			assertThatThrownBy(() -> postAppService.createPost(createBO))
					.isInstanceOf(ParamException.class)
					.hasMessage("岗位编码不能为空");
		}

		@Test
		@DisplayName("岗位名称为空时抛出ParamException")
		void createPost_emptyName_throwsParamException() {
			createBO.setPostName("");
			assertThatThrownBy(() -> postAppService.createPost(createBO))
					.isInstanceOf(ParamException.class)
					.hasMessage("岗位名称不能为空");
		}

		@Test
		@DisplayName("正常创建岗位返回岗位ID")
		void createPost_validInput_returnsPostId() {
			when(postConvertor.toDomainFromCreateBO(createBO)).thenReturn(postDO);
			doAnswer(invocation -> {
				PostDO post = invocation.getArgument(0);
				post.setId(1L);
				return null;
			}).when(postDomainService).save(any(PostDO.class));

			Long id = postAppService.createPost(createBO);
			assertThat(id).isEqualTo(1L);
			verify(postDomainService).validatePostCodeNotExists("CEO", null);
		}
	}

	@Nested
	@DisplayName("删除岗位测试")
	class DeletePostTests {

		@Test
		@DisplayName("ID为null时抛出ParamException")
		void deletePost_nullId_throwsParamException() {
			assertThatThrownBy(() -> postAppService.deletePost(null))
					.isInstanceOf(ParamException.class)
					.hasMessage("岗位ID不合法");
		}

		@Test
		@DisplayName("ID为0时抛出ParamException")
		void deletePost_zeroId_throwsParamException() {
			assertThatThrownBy(() -> postAppService.deletePost(0L))
					.isInstanceOf(ParamException.class)
					.hasMessage("岗位ID不合法");
		}
	}

	@Nested
	@DisplayName("查询岗位测试")
	class QueryPostTests {

		@Test
		@DisplayName("ID为null时抛出ParamException")
		void getPostById_nullId_throwsParamException() {
			assertThatThrownBy(() -> postAppService.getPostById(null))
					.isInstanceOf(ParamException.class)
					.hasMessage("岗位ID不合法");
		}
	}
}
