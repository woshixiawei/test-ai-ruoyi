package com.xiawei.testruoyi.learn.server.infrastructure.convertor;

import com.xiawei.testruoyi.learn.sdk.cmd.notice.NoticeCmd;
import com.xiawei.testruoyi.learn.sdk.dto.notice.NoticeDTO;
import com.xiawei.testruoyi.learn.server.application.bo.notice.NoticeBO;
import com.xiawei.testruoyi.learn.server.application.bo.notice.NoticeCreateBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.NoticeDO;
import com.xiawei.testruoyi.learn.server.infrastructure.po.NoticePO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * 通知公告对象转换器
 */
@Mapper(componentModel = "spring")
public interface NoticeConvertor {

	NoticeDO toDomain(NoticePO po);
	List<NoticeDO> toDomainList(List<NoticePO> poList);
	NoticePO toPO(NoticeDO domain);

	NoticeBO toBO(NoticeDO domain);
	List<NoticeBO> toBOList(List<NoticeDO> domainList);
	NoticeDTO toDTOFromBO(NoticeBO bo);
	List<NoticeDTO> toDTOFromBOList(List<NoticeBO> boList);

	@Mapping(target = "publisherId", ignore = true)
	@Mapping(target = "publishTime", ignore = true)
	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "updateTime", ignore = true)
	NoticeDO toDomainFromCreateBO(NoticeCreateBO bo);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "publisherId", ignore = true)
	@Mapping(target = "publishTime", ignore = true)
	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "updateTime", ignore = true)
	void updateDomainFromBO(NoticeCreateBO bo, @MappingTarget NoticeDO domain);

	NoticeCreateBO toCreateBO(NoticeCmd cmd);
}
