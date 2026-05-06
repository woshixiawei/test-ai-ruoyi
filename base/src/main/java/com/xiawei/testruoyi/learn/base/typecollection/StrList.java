package com.xiawei.testruoyi.learn.base.typecollection;

import cn.hutool.core.collection.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 字符串类型列表
 */
public class StrList extends ArrayList<String> {

    @Serial
    private static final long serialVersionUID = -3381141325965799945L;

    public StrList(int initialCapacity) {
        super(initialCapacity);
    }

    public StrList() {
    }

    public StrList(@NotNull Collection<? extends String> c) {
        super(c);
    }

    /**
     * 获取一个空的字符串List
     *
     * @return 空的字符串列表
     */
    public static StrList empty() {
        return new StrList(ListUtil.empty());
    }

    /**
     * 根据指定的元素创建一个StrList
     *
     * @param elements 元素列表
     * @return 创建好的StrList
     */
    public static StrList of(String... elements) {
        StrList list = new StrList();
        if (elements != null) {
            for (String element : elements) {
                list.add(element);
            }
        }
        return list;
    }
}
