package com.xiawei.testruoyi.learn.server.application.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.io.FileUtil;
import com.xiawei.testruoyi.learn.base.exceptions.BusinessException;
import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.application.bo.file.FileBO;
import com.xiawei.testruoyi.learn.server.application.service.FileAppService;
import com.xiawei.testruoyi.learn.server.domain.user.service.FileDomainService;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.FileConvertor;
import com.xiawei.testruoyi.learn.server.infrastructure.po.FilePO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * 文件应用服务实现
 *
 * <p>只通过 FileDomainService 访问数据层，不直接操作Repository。</p>
 * <p>Convertor调用集中在AppService层，Controller不直接操作Convertor。</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileAppServiceImpl implements FileAppService {

	private final FileDomainService fileDomainService;
	private final FileConvertor fileConvertor;

	/** 文件存储根路径，实际项目中应从配置读取 */
	private static final String UPLOAD_DIR = "./uploads/";

	/** 允许上传的文件扩展名 */
	private static final java.util.Set<String> ALLOWED_EXTENSIONS = java.util.Set.of(
			"jpg", "jpeg", "png", "gif", "bmp", "webp",
			"doc", "docx", "xls", "xlsx", "ppt", "pptx",
			"pdf", "txt", "zip", "rar"
	);

	/** 最大文件大小：10MB */
	private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public FileBO upload(MultipartFile file) {
		// 1. 应用层参数校验
		if (file == null || file.isEmpty()) {
			throw new ParamException("上传文件不能为空");
		}

		// 2. 文件大小校验
		if (file.getSize() > MAX_FILE_SIZE) {
			throw new ParamException("文件大小不能超过10MB");
		}

		// 3. 文件类型校验
		String originalFilename = file.getOriginalFilename();
		String ext = originalFilename != null && originalFilename.contains(".")
				? originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase() : "";
		if (ext.isEmpty() || !ALLOWED_EXTENSIONS.contains(ext)) {
			throw new ParamException("不支持的文件类型：" + ext);
		}

		// 4. 生成唯一文件名
		String storedName = UUID.randomUUID().toString().replace("-", "") + "." + ext;

		// 5. 确保目录存在
		File uploadDir = new File(UPLOAD_DIR);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		// 6. 保存文件到磁盘
		String filePath = UPLOAD_DIR + storedName;
		try {
			file.transferTo(new File(filePath));
		} catch (IOException e) {
			throw new BusinessException("文件保存失败：" + e.getMessage());
		}

		// 7. 保存文件记录到数据库
		FilePO filePO = new FilePO();
		filePO.setFileName(originalFilename);
		filePO.setFilePath(filePath);
		filePO.setFileSize(file.getSize());
		filePO.setFileType(ext);
		filePO.setUrl("/backend/system/file/download/" + storedName);
		filePO.setCreateBy(StpUtil.getLoginIdAsLong());
		filePO.setCreateTime(new Date());
		filePO.setUpdateTime(new Date());
		fileDomainService.save(filePO);

		log.info("[FileAppService] 文件上传成功, fileId={}, fileName={}", filePO.getId(), originalFilename);
		return fileConvertor.toBO(filePO);
	}

	@Override
	public PageBO<FileBO> pageFiles(long current, long size) {
		PageDO<FilePO> poPage = fileDomainService.findPage(current, size);
		return PageBO.from(poPage, fileConvertor.toBOList(poPage.getRecords()));
	}

	@Override
	public FileBO getFileById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("文件ID不合法");
		}
		FilePO filePO = fileDomainService.getById(id);
		return fileConvertor.toBO(filePO);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteFile(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("文件ID不合法");
		}
		FilePO filePO = fileDomainService.getById(id);
		// 删除物理文件
		FileUtil.del(filePO.getFilePath());
		fileDomainService.deleteById(id);
		log.info("[FileAppService] 文件删除成功, fileId={}", id);
	}
}
