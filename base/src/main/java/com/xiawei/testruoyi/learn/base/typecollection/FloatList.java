package com.xiawei.testruoyi.learn.base.typecollection;

import cn.hutool.core.collection.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Float类型List
 */
public class FloatList extends ArrayList<Float> {

    @Serial
    private static final long serialVersionUID = 4187621500755987237L;

    public FloatList(int initialCapacity) {
        super(initialCapacity);
    }

    public FloatList() {
    }

    public FloatList(@NotNull Collection<? extends Float> c) {
        super(c);
    }

    /**
     * 获取一个空的Float列表
     *
     * @return 获取到的空的Float列表
     */
    public static FloatList empty() {
        return new FloatList(ListUtil.empty());
    }

    /**
     * 根据指定的元素创建一个FloatList
     *
     * @param elements 元素列表
     * @return 创建好的FloatList
     */
    public static FloatList of(Float... elements) {
        FloatList list = new FloatList();
        if (elements != null) {
            for (Float element : elements) {
                list.add(element);
            }
        }
        return list;
    }
}
