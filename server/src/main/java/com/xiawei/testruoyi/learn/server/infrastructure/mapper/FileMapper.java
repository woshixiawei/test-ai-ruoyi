package com.xiawei.testruoyi.learn.server.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiawei.testruoyi.learn.server.infrastructure.po.FilePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件管理 Mapper
 */
@Mapper
public interface FileMapper extends BaseMapper<FilePO> {
}
