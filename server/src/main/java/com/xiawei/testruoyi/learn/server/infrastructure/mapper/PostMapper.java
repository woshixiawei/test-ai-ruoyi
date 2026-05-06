package com.xiawei.testruoyi.learn.server.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.PostPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 岗位 Mapper
 */
@Mapper
public interface PostMapper extends BaseMapper<PostPO> {
}
