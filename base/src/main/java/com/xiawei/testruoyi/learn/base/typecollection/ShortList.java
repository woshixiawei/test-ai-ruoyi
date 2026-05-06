package com.xiawei.testruoyi.learn.base.typecollection;

import cn.hutool.core.collection.ListUtil;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 短整型列表
 */
public class ShortList extends ArrayList<Short> {

    @Serial
    private static final long serialVersionUID = 2692679550336072044L;

    public ShortList(int initialCapacity) {
        super(initialCapacity);
    }

    public ShortList() {
    }

    public ShortList(@org.jetbrains.annotations.NotNull Collection<? extends Short> c) {
        super(c);
    }

    /**
     * 获取一个空的不可变的ShortList
     *
     * @return 获取到的空的ShortList
     */
    public static ShortList empty() {
        return new ShortList(ListUtil.empty());
    }

    /**
     * 根据指定的元素创建一个ShortList
     *
     * @param elements 元素列表
     * @return 创建好的ShortList
     */
    public static ShortList of(Short... elements) {
        ShortList list = new ShortList();
        if (elements != null) {
            for (Short element : elements) {
                list.add(element);
            }
        }
        return list;
    }

}
