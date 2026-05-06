package com.xiawei.testruoyi.learn.base.util;

import cn.hutool.crypto.SecureUtil;

/**
 * 公共工具类
 */
public class CommUtil {
    /**
     * 生成加密密码
     * 将密码和盐值拼接后进行SHA1加密，用于密码存储的安全处理
     *
     * @param pwd  原始密码
     * @param salt 盐值，用于增加密码加密的复杂度
     * @return 加密后的密码字符串
     */
    public static String genPwd(String pwd, String salt) {
        return SecureUtil.sha1(pwd + ":" + salt);
    }
}