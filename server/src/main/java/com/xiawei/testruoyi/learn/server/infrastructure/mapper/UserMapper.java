package com.xiawei.testruoyi.learn.server.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.UserPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper
 *
 * <p>无需自定义方法，所有查询通过 BaseMapper + lambdaQuery() / Wrappers.lambdaQuery() 实现。</p>
 */
@Mapper
public interface UserMapper extends BaseMapper<UserPO> {
}
