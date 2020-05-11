package com.feitianzhu.huangliwo.utils;

import android.os.SystemClock;

/**
 * Created by bch on 2020/5/11
 */
public class ClickUtil {
    private static final String TAG = "ClickUtil";
    //点击间隔
    private static final long minimumClickInterval = 500;

    private static long lastClickTimestamp = 0;

    /**
     * 防止多次点击
     *
     * @return
     */
    public static boolean onClick() {
        long current = SystemClock.elapsedRealtime();
        long interval = current - lastClickTimestamp;
        lastClickTimestamp = current;
        if (interval < minimumClickInterval) {
            return false;
        }
        return true;
    }
}
