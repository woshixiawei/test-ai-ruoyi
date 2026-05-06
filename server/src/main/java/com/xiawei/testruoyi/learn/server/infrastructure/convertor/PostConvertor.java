package com.xiawei.testruoyi.learn.server.infrastructure.convertor;

import com.xiawei.testruoyi.learn.sdk.cmd.post.PostCmd;
import com.xiawei.testruoyi.learn.sdk.dto.post.PostDTO;
import com.xiawei.testruoyi.learn.server.application.bo.post.PostBO;
import com.xiawei.testruoyi.learn.server.application.bo.post.PostCreateBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.PostDO;
import com.xiawei.testruoyi.learn.server.infrastructure.po.PostPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * 岗位对象转换器
 */
@Mapper(componentModel = "spring")
public interface PostConvertor {

	PostDO toDomain(PostPO po);
	List<PostDO> toDomainList(List<PostPO> poList);
	PostPO toPO(PostDO domain);

	PostBO toBO(PostDO domain);
	List<PostBO> toBOList(List<PostDO> domainList);
	PostDTO toDTOFromBO(PostBO bo);
	List<PostDTO> toDTOFromBOList(List<PostBO> boList);

	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "updateTime", ignore = true)
	PostDO toDomainFromCreateBO(PostCreateBO bo);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "updateTime", ignore = true)
	void updateDomainFromBO(PostCreateBO bo, @MappingTarget PostDO domain);

	PostCreateBO toCreateBO(PostCmd cmd);
}
