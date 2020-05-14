package com.feitianzhu.huangliwo.splash.Bean;

public class AdvertisementBean {

    /**
     * msg : success
     * code : 0
     * data : {"strVal":"http://182.92.177.234/images/1/images/cfg/tSysConfig/2020/04/a01%403x.png","name":"rebate_vip_days","remark":"会员返利天数配置","id":1,"value":0,"url":"11"}
     */

    private String msg;
    private int code;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * strVal : http://182.92.177.234/images/1/images/cfg/tSysConfig/2020/04/a01%403x.png
         * name : rebate_vip_days
         * remark : 会员返利天数配置
         * id : 1
         * value : 0
         * url : 11
         */

        private String strVal;
        private String name;
        private String remark;
        private int id;
        private int value;
        private String url;

        public String getStrVal() {
            return strVal;
        }

        public void setStrVal(String strVal) {
            this.strVal = strVal;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
