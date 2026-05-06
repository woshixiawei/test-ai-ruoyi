package com.xiawei.testruoyi.learn.server.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.UserRolePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色关联 Mapper
 *
 * <p>无需自定义方法，所有查询通过 BaseMapper + Wrappers.lambdaQuery() 实现。</p>
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRolePO> {
}
