package com.feitianzhu.huangliwo.home.entity;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.home.entity
 * user: yangqinbo
 * date: 2020/1/10
 * time: 17:26
 * email: 694125155@qq.com
 */
public class NoticeModel implements Serializable {

    /**
     * pushMsg : {"msgId":8,"pushContent":"公告公告。。。。。。。。。。。。。。。。。发工资了。然后今晚开趴体"}
     */

    private PushMsgBean pushMsg;

    public PushMsgBean getPushMsg() {
        return pushMsg;
    }

    public void setPushMsg(PushMsgBean pushMsg) {
        this.pushMsg = pushMsg;
    }

    public static class PushMsgBean {
        /**
         * msgId : 8
         * pushContent : 公告公告。。。。。。。。。。。。。。。。。发工资了。然后今晚开趴体
         */

        private int msgId;
        private String pushContent;

        public int getMsgId() {
            return msgId;
        }

        public void setMsgId(int msgId) {
            this.msgId = msgId;
        }

        public String getPushContent() {
            return pushContent;
        }

        public void setPushContent(String pushContent) {
            this.pushContent = pushContent;
        }
    }
}
