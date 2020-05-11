package com.feitianzhu.huangliwo.model;

import com.feitianzhu.huangliwo.http.LzyResponse;

import java.io.Serializable;

/**
 * Created by jiangdikai on 2017/9/16.
 */

public class UpdateAppModel implements Serializable {

    public int code;
    public UpdateAppInfo data;
    public static class UpdateAppInfo implements Serializable{
        /**
         * versionCode : 1
         * versionName : android-1.0
         * packName : android-1.0
         * updateDesc : build-1
         * downloadUrl : https://www.pgyer.com/udid
         * isForceUpdate : 0
         */

        public int versionCode;
//        服务器版本号
        public String versionName;
        public String packName;
        //包大小
        public String packSize;
        //更新内容
        public String updateDesc;
        //下载地址
        public String downloadUrl;
        //是否强制更新 1 是 0 不是
        public String isForceUpdate;
    }


}
