package com.xiawei.testruoyi.learn.server.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.RolePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色 Mapper
 */
@Mapper
public interface RoleMapper extends BaseMapper<RolePO> {
}
