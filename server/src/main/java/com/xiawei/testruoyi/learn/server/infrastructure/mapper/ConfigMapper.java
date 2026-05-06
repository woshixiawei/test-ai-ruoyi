package com.xiawei.testruoyi.learn.server.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.ConfigPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 参数配置 Mapper
 */
@Mapper
public interface ConfigMapper extends BaseMapper<ConfigPO> {
}
