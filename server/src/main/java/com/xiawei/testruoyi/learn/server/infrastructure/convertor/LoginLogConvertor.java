package com.xiawei.testruoyi.learn.server.infrastructure.convertor;

import com.xiawei.testruoyi.learn.sdk.dto.log.LoginLogDTO;
import com.xiawei.testruoyi.learn.server.application.bo.log.LoginLogBO;
import com.xiawei.testruoyi.learn.server.infrastructure.po.LoginLogPO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 登录日志对象转换器（MapStruct）
 *
 * <p>定义在infrastructure.convertor包下，由Spring管理。</p>
 * <p>命名规范：XxxConvertor（英式拼写）。</p>
 * <p>所有对象间转换必须通过本接口，禁止手动setter转换。</p>
 */
@Mapper(componentModel = "spring")
public interface LoginLogConvertor {

	// ========== PO → BO（基础设施层 → 应用层） ==========

	/** PO -> BO */
	LoginLogBO toBO(LoginLogPO po);

	/** PO列表 -> BO列表 */
	List<LoginLogBO> toBOList(List<LoginLogPO> poList);

	// ========== BO → DTO（应用层 → SDK层，Controller调用） ==========

	/** BO -> DTO */
	LoginLogDTO toDTOFromBO(LoginLogBO bo);

	/** BO列表 -> DTO列表 */
	List<LoginLogDTO> toDTOFromBOList(List<LoginLogBO> boList);
}
