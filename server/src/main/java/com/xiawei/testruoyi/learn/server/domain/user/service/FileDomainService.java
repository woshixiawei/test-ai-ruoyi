package com.xiawei.testruoyi.learn.server.domain.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.exceptions.BusinessException;
import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.domain.user.repository.FileRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.po.FilePO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 文件领域服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileDomainService {

	private final FileRepository fileRepository;

	/** 保存文件记录 */
	public void save(FilePO filePO) {
		fileRepository.saveOrUpdate(filePO);
	}

	/** 根据ID查询文件 */
	public FilePO getById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("文件ID不合法");
		}
		FilePO po = fileRepository.getById(id);
		if (po == null) {
			throw new BusinessException("文件不存在");
		}
		return po;
	}

	/** 删除文件 */
	public void deleteById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("文件ID不合法");
		}
		fileRepository.removeById(id);
	}

	/** 分页查询文件 */
	public PageDO<FilePO> findPage(long current, long size) {
		Page<FilePO> poPage = fileRepository.findPage(current, size);
		return PageDO.from(poPage, poPage.getRecords());
	}
}
