package com.xiawei.testruoyi.learn.server.infrastructure.convertor;

import com.xiawei.testruoyi.learn.sdk.cmd.dept.DeptCmd;
import com.xiawei.testruoyi.learn.sdk.dto.dept.DeptDTO;
import com.xiawei.testruoyi.learn.server.application.bo.dept.DeptBO;
import com.xiawei.testruoyi.learn.server.application.bo.dept.DeptCreateBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.DeptDO;
import com.xiawei.testruoyi.learn.server.infrastructure.po.DeptPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * 部门对象转换器
 */
@Mapper(componentModel = "spring")
public interface DeptConvertor {

	DeptDO toDomain(DeptPO po);
	List<DeptDO> toDomainList(List<DeptPO> poList);
	DeptPO toPO(DeptDO domain);

	DeptBO toBO(DeptDO domain);
	List<DeptBO> toBOList(List<DeptDO> domainList);
	DeptDTO toDTOFromBO(DeptBO bo);
	List<DeptDTO> toDTOFromBOList(List<DeptBO> boList);

	@Mapping(target = "children", ignore = true)
	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "updateTime", ignore = true)
	DeptDO toDomainFromCreateBO(DeptCreateBO bo);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "children", ignore = true)
	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "updateTime", ignore = true)
	void updateDomainFromBO(DeptCreateBO bo, @MappingTarget DeptDO domain);

	DeptCreateBO toCreateBO(DeptCmd cmd);
}
