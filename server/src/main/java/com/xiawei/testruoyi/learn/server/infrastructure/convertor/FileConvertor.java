package com.xiawei.testruoyi.learn.server.infrastructure.convertor;

import com.xiawei.testruoyi.learn.sdk.dto.file.FileDTO;
import com.xiawei.testruoyi.learn.server.application.bo.file.FileBO;
import com.xiawei.testruoyi.learn.server.infrastructure.po.FilePO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 文件对象转换器（MapStruct）
 *
 * <p>定义在infrastructure.convertor包下，由Spring管理。</p>
 * <p>命名规范：XxxConvertor（英式拼写）。</p>
 * <p>所有对象间转换必须通过本接口，禁止手动setter转换。</p>
 */
@Mapper(componentModel = "spring")
public interface FileConvertor {

	// ========== PO → BO（基础设施层 → 应用层） ==========

	/** PO -> BO */
	FileBO toBO(FilePO po);

	/** PO列表 -> BO列表 */
	List<FileBO> toBOList(List<FilePO> poList);

	// ========== BO → DTO（应用层 → SDK层，Controller调用） ==========

	/** BO -> DTO */
	FileDTO toDTOFromBO(FileBO bo);

	/** BO列表 -> DTO列表 */
	List<FileDTO> toDTOFromBOList(List<FileBO> boList);
}
