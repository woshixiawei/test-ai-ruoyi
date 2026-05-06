package com.xiawei.testruoyi.learn.server.infrastructure.convertor;

import com.xiawei.testruoyi.learn.sdk.cmd.role.RoleCmd;
import com.xiawei.testruoyi.learn.sdk.dto.role.RoleDTO;
import com.xiawei.testruoyi.learn.server.application.bo.role.RoleBO;
import com.xiawei.testruoyi.learn.server.application.bo.role.RoleCreateBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.RoleDO;
import com.xiawei.testruoyi.learn.server.infrastructure.po.RolePO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * 角色对象转换器（MapStruct）
 */
@Mapper(componentModel = "spring")
public interface RoleConvertor {

	RoleDO toDomain(RolePO po);

	List<RoleDO> toDomainList(List<RolePO> poList);

	RolePO toPO(RoleDO domain);

	RoleBO toBO(RoleDO domain);

	List<RoleBO> toBOList(List<RoleDO> domainList);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "menuIds", ignore = true)
	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "updateTime", ignore = true)
	RoleDO toDomainFromCreateBO(RoleCreateBO bo);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "roleCode", ignore = true)
	@Mapping(target = "menuIds", ignore = true)
	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "updateTime", ignore = true)
	void updateDomainFromBO(RoleCreateBO bo, @MappingTarget RoleDO domain);

	// ========== Cmd → BO ==========

	RoleCreateBO toCreateBO(RoleCmd cmd);

	// ========== BO → DTO ==========

	RoleDTO toDTOFromBO(RoleBO bo);

	List<RoleDTO> toDTOFromBOList(List<RoleBO> boList);
}
