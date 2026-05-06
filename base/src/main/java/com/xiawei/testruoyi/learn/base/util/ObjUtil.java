package com.xiawei.testruoyi.learn.base.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;

/**
 * 对象工具类
 */
public class ObjUtil {
    /**
     * 复制Bean的属性的配置
     */
    private final static CopyOptions COPY_OPTIONS = new CopyOptions().setIgnoreNullValue(true);

    /**
     * 把一个Bean的属性复制到另外一个Bean,但是要排除null
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        BeanUtil.copyProperties(source, target, COPY_OPTIONS);
    }
}
