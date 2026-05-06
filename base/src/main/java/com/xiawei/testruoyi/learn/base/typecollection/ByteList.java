package com.xiawei.testruoyi.learn.base.typecollection;

import cn.hutool.core.collection.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Byte类型列表
 */
public class ByteList extends ArrayList<Byte> {

    @Serial
    private static final long serialVersionUID = 3053589172019408987L;

    public ByteList(int initialCapacity) {
        super(initialCapacity);
    }

    public ByteList() {
    }

    public ByteList(@NotNull Collection<? extends Byte> c) {
        super(c);
    }

    /**
     * 获取一个空的不可变的ByteList
     *
     * @return 获取到的空的ByteList
     */
    public static ByteList empty() {
        return new ByteList(ListUtil.empty());
    }

    /**
     * 根据指定的元素创建一个ByteList
     *
     * @param elements 元素列表
     * @return 创建好的ByteList
     */
    public static ByteList of(Byte... elements) {
        ByteList list = new ByteList();
        if (elements != null) {
            for (Byte element : elements) {
                list.add(element);
            }
        }
        return list;
    }
}
