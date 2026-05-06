package com.xiawei.testruoyi.learn.server.infrastructure.convertor;

import com.xiawei.testruoyi.learn.sdk.cmd.config.ConfigCmd;
import com.xiawei.testruoyi.learn.sdk.dto.config.ConfigDTO;
import com.xiawei.testruoyi.learn.server.application.bo.config.ConfigBO;
import com.xiawei.testruoyi.learn.server.application.bo.config.ConfigCreateBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.ConfigDO;
import com.xiawei.testruoyi.learn.server.infrastructure.po.ConfigPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * 参数配置对象转换器
 */
@Mapper(componentModel = "spring")
public interface ConfigConvertor {

	ConfigDO toDomain(ConfigPO po);
	List<ConfigDO> toDomainList(List<ConfigPO> poList);
	ConfigPO toPO(ConfigDO domain);

	ConfigBO toBO(ConfigDO domain);
	List<ConfigBO> toBOList(List<ConfigDO> domainList);
	ConfigDTO toDTOFromBO(ConfigBO bo);
	List<ConfigDTO> toDTOFromBOList(List<ConfigBO> boList);

	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "updateTime", ignore = true)
	ConfigDO toDomainFromCreateBO(ConfigCreateBO bo);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "updateTime", ignore = true)
	void updateDomainFromBO(ConfigCreateBO bo, @MappingTarget ConfigDO domain);

	ConfigCreateBO toCreateBO(ConfigCmd cmd);
}
