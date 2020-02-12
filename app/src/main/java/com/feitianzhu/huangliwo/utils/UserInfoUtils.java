package com.feitianzhu.huangliwo.utils;

import android.content.Context;

import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.login.RegisterActivity;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.google.gson.Gson;

public class UserInfoUtils {
    public static MineInfoModel getUserInfo(Context context) {
        MineInfoModel userInfo;
        String userString = SPUtils.getString(context, Constant.USER_DATA, "");
        if ("".equals(userString)) {
            userInfo = new MineInfoModel();
        } else {
            userInfo = new Gson().fromJson(userString, MineInfoModel.class);
        }
        return userInfo;
    }

    public static void saveUserInfo(Context context, MineInfoModel userInfo) {
        if (userInfo == null) {
            return;
        }
        String newUser = new Gson().toJson(userInfo);
        SPUtils.putString(context, Constant.USER_DATA, newUser);
    }
}
