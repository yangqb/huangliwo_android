package com.feitianzhu.huangliwo.core;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.feitianzhu.huangliwo.R;
import com.hjq.toast.ToastUtils;

import cc.shinichi.library.tool.ui.ToastUtil;

/**
 * Created by bch on 2020/5/28
 */
public class SkipToUtil {

    //跳转浏览器
    public static void toBrowser(Activity activity, String url) {
        if (url == null || url == "") {
            ToastUtils.show("参数为空");
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        activity.startActivity(intent);
    }

    //跳转其他APP
    public static void toOtherAPP(Activity activity, String url) {
        if (url == null || url == "") {
            ToastUtils.show("参数为空");
            return;
        }
        //  跳转其他APP
        PackageManager packageManager = activity.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(url);
        if (intent == null) {
            Log.e("TAG", "toOtherAPP: ");
            intent = new Intent(url);
        }
        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
