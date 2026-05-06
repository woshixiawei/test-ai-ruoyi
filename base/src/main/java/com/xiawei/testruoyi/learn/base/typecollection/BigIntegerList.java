package com.xiawei.testruoyi.learn.base.typecollection;

import cn.hutool.core.collection.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 大整形列表
 */
public class BigIntegerList extends ArrayList<BigInteger> {

    @Serial
    private static final long serialVersionUID = 6060811751552531902L;

    public BigIntegerList(int initialCapacity) {
        super(initialCapacity);
    }

    public BigIntegerList() {
    }

    public BigIntegerList(@NotNull Collection<? extends BigInteger> c) {
        super(c);
    }

    /**
     * 获取空的不可变的BigIntegerList列表
     *
     * @return 获取的BigIntegerList列表
     */
    public static BigIntegerList empty() {
        return new BigIntegerList(ListUtil.empty());
    }

    /**
     * 根据指定的元素创建一个BigIntegerList
     *
     * @param elements 元素列表
     * @return 创建好的BigIntegerList
     */
    public static BigIntegerList of(BigInteger... elements) {
        BigIntegerList list = new BigIntegerList();
        if (elements != null) {
            for (BigInteger element : elements) {
                list.add(element);
            }
        }
        return list;
    }
}
