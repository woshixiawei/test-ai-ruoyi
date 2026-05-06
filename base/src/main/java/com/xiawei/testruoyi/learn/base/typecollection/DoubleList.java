package com.xiawei.testruoyi.learn.base.typecollection;

import cn.hutool.core.collection.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Double类型的List
 */
public class DoubleList extends ArrayList<Double> {

    @Serial
    private static final long serialVersionUID = 4345495727973585984L;

    public DoubleList(int initialCapacity) {
        super(initialCapacity);
    }

    public DoubleList() {
    }

    public DoubleList(@NotNull Collection<? extends Double> c) {
        super(c);
    }

    /**
     * 获取一个空的不可变的空的DoubleList
     *
     * @return 获取到的空的DoubleList
     */
    public static DoubleList empty() {
        return new DoubleList(ListUtil.empty());
    }

    /**
     * 根据指定的元素创建一个DoubleList
     *
     * @param elements 元素列表
     * @return 创建好的DoubleList
     */
    public static DoubleList of(Double... elements) {
        DoubleList list = new DoubleList();
        if (elements != null) {
            for (Double element : elements) {
                list.add(element);
            }
        }
        return list;
    }
}
