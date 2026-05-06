package com.xiawei.testruoyi.learn.base.typecollection;

import cn.hutool.core.collection.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 整形列表
 */
public class IntList extends ArrayList<Integer> {

    @Serial
    private static final long serialVersionUID = -13417970789475421L;

    public IntList(int initialCapacity) {
        super(initialCapacity);
    }

    public IntList() {
    }

    public IntList(@NotNull Collection<? extends Integer> c) {
        super(c);
    }

    /**
     * 获取一个空的不可变的IntList
     *
     * @return 获取到的IntList
     */
    public static IntList empty() {
        return new IntList(ListUtil.empty());
    }

    /**
     * 根据指定的元素创建一个IntList
     *
     * @param elements 元素列表
     * @return 创建好的IntList
     */
    public static IntList of(Integer... elements) {
        IntList list = new IntList();
        if (elements != null) {
            for (Integer element : elements) {
                list.add(element);
            }
        }
        return list;
    }
}
