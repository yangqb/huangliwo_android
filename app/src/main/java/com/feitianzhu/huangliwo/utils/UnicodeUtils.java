package com.feitianzhu.huangliwo.utils;

import android.util.Log;

/**
 * package name: com.feitianzhu.huangliwo.utils
 * user: yangqinbo
 * date: 2020/4/16
 * time: 15:00
 * email: 694125155@qq.com
 */
public class UnicodeUtils {
    /**
     * unicode 转字符串
     * ---------------------
     * 作者：子不语归来
     * 来源：CSDN
     * 原文：https://blog.csdn.net/u010123643/article/details/54019448?utm_source=copy
     */
    public static String unicode2String(String unicode) {
        Log.e("str==", "111111111" + "\\\\u");
        StringBuffer string = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {
            Log.e("str==", "22222222" + "\\\\u");
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);

            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }

    /**
     * 字符串转换unicode
     * ---------------------
     * 作者：子不语归来
     * 来源：CSDN
     * 原文：https://blog.csdn.net/u010123643/article/details/54019448?utm_source=copy
     */
    public static String string2Unicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);
            if (c < 256)//ASC11表中的字符码值不够4位,补00
            {
                unicode.append("\\u00");
            } else {
                unicode.append("\\u");
            }
            // 转换为unicode
            unicode.append(Integer.toHexString(c));
        }

        return unicode.toString();
    }
}
