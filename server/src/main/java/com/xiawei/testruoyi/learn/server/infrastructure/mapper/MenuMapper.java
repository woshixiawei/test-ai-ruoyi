package com.xiawei.testruoyi.learn.server.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.MenuPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单 Mapper
 */
@Mapper
public interface MenuMapper extends BaseMapper<MenuPO> {
}
