package com.feitianzhu.fu700.utils.doubleclick;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;

/**
 * package name: com.feitianzhu.fu700.utils
 * user: yangqinbo
 * date: 2019/12/16
 * time: 20:46
 * email: 694125155@qq.com
 */
public class XClickUtil {
    /**
     * 最近一次点击的时间
     */
    private static long mLastClickTime;
    /**
     * 最近一次点击的控件ID
     */
    private static int mLastClickViewId;

    /**
     * 是否是快速点击
     *
     * @param v              点击的控件
     * @param intervalMillis 时间间期（毫秒）
     * @return true:是，false:不是
     */
    public static boolean isFastDoubleClick(View v, long intervalMillis) {
        int viewId = v.getId();
//        long time = System.currentTimeMillis();
        long time = SystemClock.elapsedRealtime();
        long timeInterval = Math.abs(time - mLastClickTime);
        if (timeInterval < intervalMillis && viewId == mLastClickViewId) {
            Log.e("isFastDoubleClick", "true");
            return true;
        } else {
            mLastClickTime = time;
            mLastClickViewId = viewId;
            Log.e("isFastDoubleClick", "false");
            return false;
        }
    }
}
