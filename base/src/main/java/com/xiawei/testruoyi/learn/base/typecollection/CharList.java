package com.xiawei.testruoyi.learn.base.typecollection;

import cn.hutool.core.collection.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 字符List
 */
public class CharList extends ArrayList<Character> {

    @Serial
    private static final long serialVersionUID = 8838275422829355824L;

    public CharList(int initialCapacity) {
        super(initialCapacity);
    }

    public CharList() {
    }

    public CharList(@NotNull Collection<? extends Character> c) {
        super(c);
    }

    /**
     * 获取一个空的不可变的CharList
     *
     * @return 获取到的空的CharList
     */
    public static CharList empty() {
        return new CharList(ListUtil.empty());
    }

    /**
     * 根据指定的元素创建一个CharList
     *
     * @param elements 元素列表
     * @return 创建好的CharList
     */
    public static CharList of(Character... elements) {
        CharList list = new CharList();
        if (elements != null) {
            for (Character element : elements) {
                list.add(element);
            }
        }
        return list;
    }
}
