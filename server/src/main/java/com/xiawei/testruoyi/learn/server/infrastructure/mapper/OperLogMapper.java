package com.xiawei.testruoyi.learn.server.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.OperLogPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志 Mapper
 */
@Mapper
public interface OperLogMapper extends BaseMapper<OperLogPO> {
}
