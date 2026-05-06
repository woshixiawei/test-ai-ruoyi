package com.xiawei.testruoyi.learn.server.infrastructure.convertor;

import com.xiawei.testruoyi.learn.sdk.cmd.menu.MenuCmd;
import com.xiawei.testruoyi.learn.sdk.dto.menu.MenuDTO;
import com.xiawei.testruoyi.learn.server.application.bo.menu.MenuBO;
import com.xiawei.testruoyi.learn.server.application.bo.menu.MenuCreateBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.MenuDO;
import com.xiawei.testruoyi.learn.server.infrastructure.po.MenuPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * 菜单对象转换器
 */
@Mapper(componentModel = "spring")
public interface MenuConvertor {

	MenuDO toDomain(MenuPO po);
	List<MenuDO> toDomainList(List<MenuPO> poList);
	MenuPO toPO(MenuDO domain);

	MenuBO toBO(MenuDO domain);
	List<MenuBO> toBOList(List<MenuDO> domainList);
	MenuDTO toDTOFromBO(MenuBO bo);
	List<MenuDTO> toDTOFromBOList(List<MenuBO> boList);

	@Mapping(target = "children", ignore = true)
	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "updateTime", ignore = true)
	MenuDO toDomainFromCreateBO(MenuCreateBO bo);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "children", ignore = true)
	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "updateTime", ignore = true)
	void updateDomainFromBO(MenuCreateBO bo, @MappingTarget MenuDO domain);

	MenuCreateBO toCreateBO(MenuCmd cmd);
}
