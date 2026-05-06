package com.xiawei.testruoyi.learn.server.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.DictDataPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典数据 Mapper
 */
@Mapper
public interface DictDataMapper extends BaseMapper<DictDataPO> {
}
