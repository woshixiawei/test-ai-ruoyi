package com.xiawei.testruoyi.learn.server.infrastructure.convertor;

import com.xiawei.testruoyi.learn.sdk.cmd.dict.DictDataCmd;
import com.xiawei.testruoyi.learn.sdk.dto.dict.DictDataDTO;
import com.xiawei.testruoyi.learn.server.application.bo.dict.DictDataBO;
import com.xiawei.testruoyi.learn.server.application.bo.dict.DictDataCreateBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.DictDataDO;
import com.xiawei.testruoyi.learn.server.infrastructure.po.DictDataPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * 字典数据对象转换器
 */
@Mapper(componentModel = "spring")
public interface DictDataConvertor {

	DictDataDO toDomain(DictDataPO po);
	List<DictDataDO> toDomainList(List<DictDataPO> poList);
	DictDataPO toPO(DictDataDO domain);

	DictDataBO toBO(DictDataDO domain);
	List<DictDataBO> toBOList(List<DictDataDO> domainList);
	DictDataDTO toDTOFromBO(DictDataBO bo);
	List<DictDataDTO> toDTOFromBOList(List<DictDataBO> boList);

	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "updateTime", ignore = true)
	DictDataDO toDomainFromCreateBO(DictDataCreateBO bo);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "updateTime", ignore = true)
	void updateDomainFromBO(DictDataCreateBO bo, @MappingTarget DictDataDO domain);

	DictDataCreateBO toCreateBO(DictDataCmd cmd);
}
