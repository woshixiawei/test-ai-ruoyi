package com.xiawei.testruoyi.learn.base.util;

import cn.hutool.core.lang.Assert;

/**
 * 数学工具类
 */
public class MathUtil {
    /**
     * 计算能被指定数整除的最小数
     * <p>
     * 如果数A能被数B整除，则返回A；否则返回大于A的能被B整除的最小数。
     * </p>
     *
     * @param a 被除数
     * @param b 除数
     * @return 能被b整除的最小数（大于等于a）
     * @throws IllegalArgumentException 如果除数b小于等于0
     */
    public static int getDivisibleNumber(int a, int b) {
        Assert.isTrue(b > 0, () -> new IllegalArgumentException("除数必须大于0"));

        // 如果a能被b整除，直接返回a
        if (a % b == 0) {
            return a;
        }

        // 否则返回大于a的能被b整除的最小数
        // 计算公式：(a / b + 1) * b
        return (a / b + 1) * b;
    }


    /**
     * 计算能被指定数整除的最大数
     * <p>
     * 如果数A能被数B整除，则返回A；否则返回小于A的能被B整除的最大数。
     * </p>
     *
     * @param a 被除数
     * @param b 除数
     * @return 能被b整除的最大数（小于等于a）
     * @throws IllegalArgumentException 如果除数b小于等于0
     */
    public static int getMaxDivisibleNumber(int a, int b) {
        Assert.isTrue(b > 0, () -> new IllegalArgumentException("除数必须大于0"));

        // 如果a能被b整除，直接返回a
        if (a % b == 0) {
            return a;
        }

        // 否则返回小于a的能被b整除的最大数
        // 计算公式：(a / b) * b
        return (a / b) * b;
    }
}
