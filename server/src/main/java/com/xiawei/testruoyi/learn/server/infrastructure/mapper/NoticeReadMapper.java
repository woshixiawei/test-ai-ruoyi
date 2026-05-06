package com.xiawei.testruoyi.learn.server.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.NoticeReadPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 公告已读记录 Mapper
 */
@Mapper
public interface NoticeReadMapper extends BaseMapper<NoticeReadPO> {
}
