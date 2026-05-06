package com.xiawei.testruoyi.learn.base.typecollection;

import cn.hutool.core.collection.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 长整型对应的List
 */
public class LongList extends ArrayList<Long> {

    @Serial
    private static final long serialVersionUID = 2975280180056954501L;

    public LongList(int initialCapacity) {
        super(initialCapacity);
    }

    public LongList() {
    }

    public LongList(@NotNull Collection<? extends Long> c) {
        super(c);
    }

    /**
     * 获取一个空的不可变的空列表
     *
     * @return 获取到的空的LongList
     */
    public static LongList empty() {
        return new LongList(ListUtil.empty());
    }

    /**
     * 根据指定的元素创建一个LongList
     *
     * @param elements 元素列表
     * @return 创建好的LongList
     */
    public static LongList of(Long... elements) {
        LongList list = new LongList();
        if (elements != null) {
            for (Long element : elements) {
                list.add(element);
            }
        }
        return list;
    }
}
