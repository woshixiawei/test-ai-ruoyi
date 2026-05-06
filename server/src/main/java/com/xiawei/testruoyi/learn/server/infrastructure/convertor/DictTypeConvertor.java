package com.xiawei.testruoyi.learn.server.infrastructure.convertor;

import com.xiawei.testruoyi.learn.sdk.cmd.dict.DictTypeCmd;
import com.xiawei.testruoyi.learn.sdk.dto.dict.DictTypeDTO;
import com.xiawei.testruoyi.learn.server.application.bo.dict.DictTypeBO;
import com.xiawei.testruoyi.learn.server.application.bo.dict.DictTypeCreateBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.DictTypeDO;
import com.xiawei.testruoyi.learn.server.infrastructure.po.DictTypePO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * 字典类型对象转换器
 */
@Mapper(componentModel = "spring")
public interface DictTypeConvertor {

	DictTypeDO toDomain(DictTypePO po);
	List<DictTypeDO> toDomainList(List<DictTypePO> poList);
	DictTypePO toPO(DictTypeDO domain);

	DictTypeBO toBO(DictTypeDO domain);
	List<DictTypeBO> toBOList(List<DictTypeDO> domainList);
	DictTypeDTO toDTOFromBO(DictTypeBO bo);
	List<DictTypeDTO> toDTOFromBOList(List<DictTypeBO> boList);

	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "updateTime", ignore = true)
	DictTypeDO toDomainFromCreateBO(DictTypeCreateBO bo);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "updateTime", ignore = true)
	void updateDomainFromBO(DictTypeCreateBO bo, @MappingTarget DictTypeDO domain);

	DictTypeCreateBO toCreateBO(DictTypeCmd cmd);
}
