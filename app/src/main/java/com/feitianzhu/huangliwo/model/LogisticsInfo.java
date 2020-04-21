package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/4/20
 * time: 10:10
 * email: 694125155@qq.com
 */
public class LogisticsInfo implements Serializable {
    /**
     * time : 2019-12-14 19:57:33
     * ftime : 2019-12-14 19:57:33
     * context : 【深圳市】 快件已在 【深圳固戍】 签收, 签收人: 前台, 如有疑问请电联:15919433069 / 0755-81466334, 您的快递已经妥投。风里来雨里去, 只为客官您满意。上有老下有小, 赏个好评好不好？【请在评价快递员处帮忙点亮五颗星星哦~】
     */

    private String time;
    private String ftime;
    private String context;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFtime() {
        return ftime;
    }

    public void setFtime(String ftime) {
        this.ftime = ftime;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
