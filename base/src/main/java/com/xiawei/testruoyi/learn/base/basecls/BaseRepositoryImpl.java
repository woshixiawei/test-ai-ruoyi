package com.xiawei.testruoyi.learn.base.basecls;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * Repository 基础实现类
 *
 * <p>所有具体的 Repository 实现类都必须继承此类。</p>
 * <p>继承 MyBatis-Plus 的 ServiceImpl<M, T>，获得通用 CRUD 实现。</p>
 * <p>软删除通过 MyBatis-Plus 原生 @TableLogic 注解 + 全局配置实现：</p>
 * <p>- @TableLogic(value = "0", delval = "id") 确保 removeById() 生成 SET is_deleted = id</p>
 * <p>- 全局配置 logic-delete-value: id、logic-not-delete-value: 0 确保查询自动追加 WHERE is_deleted = 0</p>
 *
 * @param <M> Mapper 类型
 * @param <T> PO 实体类型
 */
public abstract class BaseRepositoryImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseRepository<T> {
}
