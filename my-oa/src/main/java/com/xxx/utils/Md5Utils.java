package com.xxx.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class Md5Utils {
    /**
     * 加密
     * @param source
     * @return
     */
    public static String md5Digest(String source) {
        return DigestUtils.md2Hex(source);
    }

    /**
     * 加盐加密
     * @param source
     * @param salt
     * @return
     */
    public static String md5Digest(String source, Integer salt) {
        char[] chars = source.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) (chars[i] + salt);
        }
        String target = new String(chars);
        return DigestUtils.md2Hex(target);
    }
}
