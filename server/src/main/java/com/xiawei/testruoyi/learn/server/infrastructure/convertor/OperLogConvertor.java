package com.xiawei.testruoyi.learn.server.infrastructure.convertor;

import com.xiawei.testruoyi.learn.sdk.dto.log.OperLogDTO;
import com.xiawei.testruoyi.learn.server.application.bo.log.OperLogBO;
import com.xiawei.testruoyi.learn.server.infrastructure.po.OperLogPO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 操作日志对象转换器（MapStruct）
 *
 * <p>定义在infrastructure.convertor包下，由Spring管理。</p>
 * <p>命名规范：XxxConvertor（英式拼写）。</p>
 * <p>所有对象间转换必须通过本接口，禁止手动setter转换。</p>
 */
@Mapper(componentModel = "spring")
public interface OperLogConvertor {

	// ========== PO → BO（基础设施层 → 应用层） ==========

	/** PO -> BO */
	OperLogBO toBO(OperLogPO po);

	/** PO列表 -> BO列表 */
	List<OperLogBO> toBOList(List<OperLogPO> poList);

	// ========== BO → DTO（应用层 → SDK层，Controller调用） ==========

	/** BO -> DTO */
	OperLogDTO toDTOFromBO(OperLogBO bo);

	/** BO列表 -> DTO列表 */
	List<OperLogDTO> toDTOFromBOList(List<OperLogBO> boList);
}
