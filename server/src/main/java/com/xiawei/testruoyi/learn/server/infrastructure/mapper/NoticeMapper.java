package com.xiawei.testruoyi.learn.server.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.NoticePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知公告 Mapper
 */
@Mapper
public interface NoticeMapper extends BaseMapper<NoticePO> {
}
