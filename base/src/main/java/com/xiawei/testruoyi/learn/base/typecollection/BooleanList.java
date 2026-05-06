package com.xiawei.testruoyi.learn.base.typecollection;

import cn.hutool.core.collection.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 布尔类型列表
 */
public class BooleanList extends ArrayList<Boolean> {

    @Serial
    private static final long serialVersionUID = -3505285720735397612L;

    public BooleanList(int initialCapacity) {
        super(initialCapacity);
    }

    public BooleanList() {
    }

    public BooleanList(@NotNull Collection<? extends Boolean> c) {
        super(c);
    }

    /**
     * 获取一个空的不可变的BooleanList
     *
     * @return 获取到的空的BooleanList
     */
    public static BooleanList empty() {
        return new BooleanList(ListUtil.empty());
    }

    /**
     * 根据指定的元素创建一个BooleanList
     *
     * @param elements 元素列表
     * @return 创建好的BooleanList
     */
    public static BooleanList of(Boolean... elements) {
        BooleanList list = new BooleanList();
        if (elements != null) {
            for (Boolean element : elements) {
                list.add(element);
            }
        }
        return list;
    }

}
