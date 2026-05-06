package com.xiawei.testruoyi.learn.server.application.service.impl;

import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.server.domain.user.service.FileDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.FileConvertor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.lang.reflect.Field;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 文件应用服务单元测试
 * 
 * <p>重点验证Code Review修复项：文件上传类型/大小校验</p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("FileAppService 单元测试")
class FileAppServiceImplTest {

	@Mock
	private FileDomainService fileDomainService;

	@Mock
	private FileConvertor fileConvertor;

	@InjectMocks
	private FileAppServiceImpl fileAppService;

	@Nested
	@DisplayName("文件上传测试")
	class UploadTests {

		@Test
		@DisplayName("文件为null时抛出ParamException")
		void upload_nullFile_throwsParamException() {
			assertThatThrownBy(() -> fileAppService.upload(null))
					.isInstanceOf(ParamException.class)
					.hasMessage("上传文件不能为空");
		}

		@Test
		@DisplayName("空文件时抛出ParamException")
		void upload_emptyFile_throwsParamException() {
			MockMultipartFile emptyFile = new MockMultipartFile(
					"file", "test.txt", "text/plain", new byte[0]);

			assertThatThrownBy(() -> fileAppService.upload(emptyFile))
					.isInstanceOf(ParamException.class)
					.hasMessage("上传文件不能为空");
		}

		@Test
		@DisplayName("文件超过10MB时抛出ParamException")
		void upload_oversizeFile_throwsParamException() {
			// 创建一个超过10MB的Mock文件
			MockMultipartFile bigFile = new MockMultipartFile(
					"file", "big.pdf", "application/pdf", new byte[11 * 1024 * 1024]);

			assertThatThrownBy(() -> fileAppService.upload(bigFile))
					.isInstanceOf(ParamException.class)
					.hasMessage("文件大小不能超过10MB");
		}

		@Test
		@DisplayName("不允许的文件类型exe抛出ParamException")
		void upload_unsupportedExtension_throwsParamException() {
			MockMultipartFile exeFile = new MockMultipartFile(
					"file", "virus.exe", "application/octet-stream", "content".getBytes());

			assertThatThrownBy(() -> fileAppService.upload(exeFile))
					.isInstanceOf(ParamException.class)
					.hasMessage("不支持的文件类型：exe");
		}

		@Test
		@DisplayName("不允许的文件类型sh抛出ParamException")
		void upload_shellScript_throwsParamException() {
			MockMultipartFile shFile = new MockMultipartFile(
					"file", "script.sh", "text/x-sh", "echo hello".getBytes());

			assertThatThrownBy(() -> fileAppService.upload(shFile))
					.isInstanceOf(ParamException.class)
					.hasMessage("不支持的文件类型：sh");
		}

		@Test
		@DisplayName("无扩展名文件抛出ParamException")
		void upload_noExtension_throwsParamException() {
			MockMultipartFile noExtFile = new MockMultipartFile(
					"file", "README", "text/plain", "content".getBytes());

			assertThatThrownBy(() -> fileAppService.upload(noExtFile))
					.isInstanceOf(ParamException.class);
		}

		@Test
		@DisplayName("允许的jpg类型在白名单中")
		void upload_jpgFile_inWhitelist() throws Exception {
			Set<String> allowedExtensions = getAllowedExtensions();
			assertThat(allowedExtensions).contains("jpg");
		}

		@Test
		@DisplayName("允许的pdf类型在白名单中")
		void upload_pdfFile_inWhitelist() throws Exception {
			Set<String> allowedExtensions = getAllowedExtensions();
			assertThat(allowedExtensions).contains("pdf");
		}
	}

	/**
	 * 通过反射获取FileAppServiceImpl的ALLOWED_EXTENSIONS常量
	 */
	@SuppressWarnings("unchecked")
	private Set<String> getAllowedExtensions() throws Exception {
		Field field = FileAppServiceImpl.class.getDeclaredField("ALLOWED_EXTENSIONS");
		field.setAccessible(true);
		return (Set<String>) field.get(null);
	}

	@Nested
	@DisplayName("文件查询测试")
	class QueryTests {

		@Test
		@DisplayName("ID为null时抛出ParamException")
		void getFileById_nullId_throwsParamException() {
			assertThatThrownBy(() -> fileAppService.getFileById(null))
					.isInstanceOf(ParamException.class)
					.hasMessage("文件ID不合法");
		}

		@Test
		@DisplayName("ID为0时抛出ParamException")
		void getFileById_zeroId_throwsParamException() {
			assertThatThrownBy(() -> fileAppService.getFileById(0L))
					.isInstanceOf(ParamException.class)
					.hasMessage("文件ID不合法");
		}

		@Test
		@DisplayName("ID为负数时抛出ParamException")
		void getFileById_negativeId_throwsParamException() {
			assertThatThrownBy(() -> fileAppService.getFileById(-1L))
					.isInstanceOf(ParamException.class)
					.hasMessage("文件ID不合法");
		}
	}

	@Nested
	@DisplayName("文件删除测试")
	class DeleteTests {

		@Test
		@DisplayName("ID为null时抛出ParamException")
		void deleteFile_nullId_throwsParamException() {
			assertThatThrownBy(() -> fileAppService.deleteFile(null))
					.isInstanceOf(ParamException.class)
					.hasMessage("文件ID不合法");
		}

		@Test
		@DisplayName("ID为0时抛出ParamException")
		void deleteFile_zeroId_throwsParamException() {
			assertThatThrownBy(() -> fileAppService.deleteFile(0L))
					.isInstanceOf(ParamException.class)
					.hasMessage("文件ID不合法");
		}
	}
}
