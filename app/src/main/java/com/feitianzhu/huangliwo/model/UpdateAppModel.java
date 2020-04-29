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
        public String versionName;
        public String packName;
        public String packSize;
        public String updateDesc;
        public String downloadUrl;
        public String isForceUpdate;
    }


}
