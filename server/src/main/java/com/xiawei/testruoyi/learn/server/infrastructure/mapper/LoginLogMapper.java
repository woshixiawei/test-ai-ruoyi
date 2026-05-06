package com.xiawei.testruoyi.learn.server.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.LoginLogPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录日志 Mapper
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLogPO> {
}
