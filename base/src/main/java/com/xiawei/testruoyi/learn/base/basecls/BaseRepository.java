package com.xiawei.testruoyi.learn.base.basecls;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * Repository 基础接口
 *
 * <p>所有具体的 Repository 接口都必须继承此接口。</p>
 * <p>继承 MyBatis-Plus 的 IService<T>，获得通用 CRUD 能力。</p>
 * <p>自定义查询方法使用 LambdaQueryWrapper / LambdaQueryChainWrapper 构建。</p>
 *
 * @param <T> PO 实体类型
 */
public interface BaseRepository<T> extends IService<T> {

}
