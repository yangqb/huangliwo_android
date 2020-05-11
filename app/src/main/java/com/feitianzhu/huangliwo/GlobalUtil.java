package com.feitianzhu.huangliwo;

import android.app.Application;
import android.content.Context;
import android.support.v4.BuildConfig;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;

import com.feitianzhu.huangliwo.utils.ScreenUtil;

/**
 * Created by bch on 2020/5/11
 * 全局对象保存
 * 全局操作,方便调用
 */
public class GlobalUtil {
    private static Application application;
    /**
     * APP的主activity
     */
    private static FragmentActivity mainActivity;
    /**
     * 当前的activity
     */
    private static FragmentActivity currentActivity;

    public static void setApplication(Application application) {
        GlobalUtil.application = application;
    }

    /**
     * 获取APP对象
     *
     * @return
     */
    public static Application getApplication() {
        return application;
    }

    /**
     * 设置主Activity
     *
     * @param mainActivity
     */
    public static void setMainActivity(FragmentActivity mainActivity) {
        GlobalUtil.mainActivity = mainActivity;
        currentActivity = mainActivity;
    }

    /**
     * 获取主Activity
     *
     * @return
     */
    public static FragmentActivity getMainActivity() {
        return mainActivity;
    }

    /**
     * 设置当前Activity
     *
     * @param currentActivity
     */
    public static void setCurrentActivity(FragmentActivity currentActivity) {
        GlobalUtil.currentActivity = currentActivity;
    }

    /**
     * @return
     */
    public static FragmentActivity getCurrentActivity() {
        return currentActivity;
    }

    /**
     * 判断apk包是否是debug模式
     */
    public static boolean isDebugMode() {
        return BuildConfig.DEBUG;
    }

    public static int px2dp(float pxValue) {
        return ScreenUtil.px2dp(mainActivity, pxValue);
    }

    public static int dp2px(float dipValue) {
        return ScreenUtil.dp2px(mainActivity, dipValue);
    }

    public static String getString(int resid, Object... formatArgs) {
        return application.getResources().getString(resid, formatArgs);
    }

    public static Spanned getHtmlString(int resid, Object... formatArgs) {
        return Html.fromHtml(application.getResources().getString(resid, formatArgs));
    }

    public static String getStringSafe(int resid) {
        try {
            return application.getResources().getString(resid);
        } catch (Exception e) {
            return "";
        }
    }
}
