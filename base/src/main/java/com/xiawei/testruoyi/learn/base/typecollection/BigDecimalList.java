package com.xiawei.testruoyi.learn.base.typecollection;

import cn.hutool.core.collection.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

/**
 * BigDecimal对应的列表数据
 */
public class BigDecimalList extends ArrayList<BigDecimal> {

    @Serial
    private static final long serialVersionUID = 5701469494139495697L;

    public BigDecimalList(int initialCapacity) {
        super(initialCapacity);
    }

    public BigDecimalList() {
    }

    public BigDecimalList(@NotNull Collection<? extends BigDecimal> c) {
        super(c);
    }

    /**
     * 获取空的不可变的列表
     *
     * @return 获取到的空列表
     */
    public static BigDecimalList empty() {
        return new BigDecimalList(ListUtil.empty());
    }

    /**
     * 根据指定的元素创建一个BigDecimalList
     *
     * @param elements 元素列表
     * @return 创建好的BigDecimalList
     */
    public static BigDecimalList of(BigDecimal... elements) {
        BigDecimalList list = new BigDecimalList();
        if (elements != null) {
            for (BigDecimal element : elements) {
                list.add(element);
            }
        }
        return list;
    }
}
