package com.xiawei.testruoyi.learn.server.application.service;

import com.xiawei.testruoyi.learn.base.model.PageBO;
import com.xiawei.testruoyi.learn.server.application.bo.file.FileBO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件应用服务接口
 *
 * <p>入参和出参对象类型必须以 BO 结尾，禁止暴露Cmd/DTO等其他类型</p>
 */
public interface FileAppService {

	/** 文件上传 */
	FileBO upload(MultipartFile file);

	/** 文件分页列表 */
	PageBO<FileBO> pageFiles(long current, long size);

	/** 文件详情 */
	FileBO getFileById(Long id);

	/** 删除文件 */
	void deleteFile(Long id);
}
