package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.fu700.model
 * user: yangqinbo
 * date: 2019/12/19
 * time: 16:37
 * email: 694125155@qq.com
 */
public class LogisticsModel implements Serializable {

    /**
     * message : ok
     * nu : 75320050643223
     * ischeck : 1
     * condition : D01
     * com : zhongtong
     * status : 200
     * state : 3
     * data : [{"time":"2019-12-14 19:57:33","ftime":"2019-12-14 19:57:33","context":"【深圳市】 快件已在 【深圳固戍】 签收, 签收人: 前台, 如有疑问请电联:15919433069 / 0755-81466334, 您的快递已经妥投。风里来雨里去, 只为客官您满意。上有老下有小, 赏个好评好不好？【请在评价快递员处帮忙点亮五颗星星哦~】"},{"time":"2019-12-14 15:49:57","ftime":"2019-12-14 15:49:57","context":"【深圳市】 【深圳固戍】 的三围卢伟成派（15919433069） 正在第1次派件, 请保持电话畅通,并耐心等待（95720为中通快递员外呼专属号码，请放心接听）"},{"time":"2019-12-14 11:56:14","ftime":"2019-12-14 11:56:14","context":"【深圳市】 快件已经到达 【深圳固戍】"},{"time":"2019-12-14 09:39:24","ftime":"2019-12-14 09:39:24","context":"【深圳市】 快件离开 【深圳中心】 已发往 【深圳固戍】"},{"time":"2019-12-14 09:30:49","ftime":"2019-12-14 09:30:49","context":"【深圳市】 快件已经到达 【深圳中心】"},{"time":"2019-12-14 05:27:15","ftime":"2019-12-14 05:27:15","context":"【东莞市】 快件离开 【东莞中心】 已发往 【深圳中心】"},{"time":"2019-12-14 05:23:56","ftime":"2019-12-14 05:23:56","context":"【东莞市】 快件已经到达 【东莞中心】"},{"time":"2019-12-13 14:07:40","ftime":"2019-12-13 14:07:40","context":"【东莞市】 快件离开 【东莞沙田】 已发往 【东莞中心】"},{"time":"2019-12-13 14:07:35","ftime":"2019-12-13 14:07:35","context":"【东莞市】 【东莞沙田】（0769-38925878、0769-38925875） 的 谢治国（13431506128） 已揽收"}]
     */

    private String message;
    private String nu;
    private int ischeck;
    private String condition;
    private String com;
    private int status;
    private int state;
    private List<DataBean> data;

    public int getIscheck() {
        return ischeck;
    }

    public void setIscheck(int ischeck) {
        this.ischeck = ischeck;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNu() {
        return nu;
    }

    public void setNu(String nu) {
        this.nu = nu;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
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
}
