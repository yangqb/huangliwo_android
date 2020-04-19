package com.feitianzhu.huangliwo.utils;

import android.content.Context;

import com.hjq.toast.style.ToastBlackStyle;

/**
 * package name: com.feitianzhu.huangliwo.utils
 * user: yangqinbo
 * date: 2020/4/18
 * time: 11:20
 * email: 694125155@qq.com
 */
public class ToastWhiteStyle2 extends ToastBlackStyle {
    public ToastWhiteStyle2(Context context) {
        super(context);
    }
    @Override
    public int getBackgroundColor() {
        return 0XFFFFFFFF;
    }

    @Override
    public int getTextColor() {
        return 0XBB000000;
    }
}
