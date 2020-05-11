package com.feitianzhu.huangliwo.core.network;

/**
 * Created by bch on 2020/5/11
 */
public interface AbsApiRequestLife {

    public boolean showFailToast = false;


    /**
     * 在将返回值转换成对应的model之前,对返回值进行一些处理
     * 第一次处理之后
     * 第二次处理之前
     */
    Object handleRsponseBeforeTransform(Object rsp);

    /**
     * 在将返回值转换成对应的model后,对返回值进行一些处理
     * 完成了第二次处理
     */
    Object handleRsponseAfterTransform(Object rsp);

    /**
     * 是否展示成功提示
     *
     * @return
     */
    boolean getshowSuccessToast();

    /**
     * 是否展示错误提示
     *
     * @return
     */
    boolean getshowFailToast();

    /**
     * 获取成功提示内容
     */
    String getSuccessToast();

    /**
     * 获取失败提示内容
     */
    String getFailToast();

    /**
     * 调用展示提示
     */
    void showToast(boolean success, int code, String message);
}
