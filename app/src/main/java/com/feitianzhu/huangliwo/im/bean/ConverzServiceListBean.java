package com.feitianzhu.huangliwo.im.bean;

public class ConverzServiceListBean {

    /**
     * id : 1
     * phone : 123456789
     * nick : 商品客服
     * icon : http://bldby-dev.oss-cn-beijing.aliyuncs.com/IMImages/1591074572146.png?Expires=1906434565&OSSAccessKeyId=LTAI4GHEkb4SUqKsjoLMcw1d&Signature=PVTYn4gosAQflW6qoBBBhNm0VHU%3D
     * userId : 1
     * type : 1
     */

    private int id;
    private String phone;
    private String nick;
    private String icon;
    private int userId;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
