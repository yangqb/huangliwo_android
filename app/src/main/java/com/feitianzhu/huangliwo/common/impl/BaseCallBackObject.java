package com.feitianzhu.huangliwo.common.impl;

import com.google.gson.Gson;

import okhttp3.Response;

/**
 * Created by dicallc on 2017/9/6 0006.
 */

public  class BaseCallBackObject extends BaseCallBackT {


    private final Class mClass;

    public BaseCallBackObject(onNetFinishLinstenerT mLinstener, Class mClass) {
        super(mLinstener);
        this.mClass = mClass;
    }

    @Override
    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
        Object o = new Gson().fromJson(mData, mClass);
        return o;
    }

}
