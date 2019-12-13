package com.feitianzhu.fu700.utils;

/**
 * Created by Lee on 2017/9/3.
 */

public class Urls {

    //测试地址
    //private static final String HOST_URL = "http://39.106.65.35";
    //本地地址
    private static final String HOST_URL = "http://192.168.0.21";

    private static final String HOST_PORT = "8089";

    private static final String BASE_URL = HOST_URL + ":" + HOST_PORT + "/";

    /**
     * 注册
     */
    public static final String REGISTER = BASE_URL + "fhwl/commons/account/registeruser";

    /**
     * 登录
     */
    public static final String LOGIN = BASE_URL + "fhwl/commons/account/login";

    /**
     * 获取手机验证码：ype 1-注册 2-更换手机号码(旧号码) 3-更换手机号码(新号码) 4-找回密码 5-设置二级密码 6-找回二级密码
     */
    public static final String GET_SMSCODE = BASE_URL + "fhwl/commons/getsmscode";

    /**
     * 获取首页数据
     */
    public static final String GET_INDEX = BASE_URL + "fhwl/index/getindex";

    /*
     * 获取商城分类
     * */
    public static final String GET_SHOP_CLASS = BASE_URL + "fhwl/shop/getshop";

    /*
     * 商城分类的商品
     * */
    public static final String GET_SHOP = BASE_URL + "fhwl/shop/getclsid";

    /*
     * 商品详情数据
     * */
    public static final String GET_SHOP_DETAIL = BASE_URL + "fhwl/shop/getGoodsId";

    /*
     * 商品规格参数
     * */
    public static final String GET_PRODUCT_PARAMETERS = BASE_URL + "fhwl/shop/getskuValue";

    /*
     * 所有订单列表数据
     * */
    public static final String GET_ORDER_INFO = BASE_URL + "fhwl/order/svbill/goodsorder";

    /*
     * 添加地址
     * */
    public static final String ADD_ADDRESS = BASE_URL + "fhwl/shop/addUserAddr";

    /*
     * 删除地址
     * */
    public static final String DELETE_ADDRESS = BASE_URL + "fhwl/shop/deleteAddr";

    /*
     * 修改地址
     * */
    public static final String UPDATA_ADDRESS = BASE_URL + "fhwl/shop/upateAddr";

    /*
     * 地址列表
     * */
    public static final String GET_ADDRESS = BASE_URL + "fhwl/shop/selectUserAddr";

    /**
     * 更换手机号
     */
    public static final String UPDATE_PHONE = BASE_URL + "fhwl/user/phone/updatephone";

    /**
     * 查询个人信息
     */
    public static final String GET_USERINFO = BASE_URL + "fhwl/user/getuserinfo";

    /**
     * 修改密码
     */
    public static final String UPDATE_ULPASS = BASE_URL + "fhwl/user/updateulpass";

    /**
     * 设置二级密码
     */
    public static final String SET_PAYPASS = BASE_URL + "fhwl/user/setpaypass";

    /**
     * 重置二级密码
     */
    public static final String UPDATE_UPAYPASS = BASE_URL + "fhwl/user/updateupaypass";

    /**
     * 找回登录密码
     */
    public static final String GET_MYPASSWORD = BASE_URL + "fhwl/user/getmypassword";

    /**
     * 找回二级支付密码
     */
    public static final String GET_UPAYPASS = BASE_URL + "fhwl/user/getupaypass";

    /**
     * 二级密码校验
     */
    public static final String CHECK_PAYPASS = BASE_URL + "fhwl/user/checkpaypass";

    /**
     * 意见反馈
     */
    public static final String USER_FEEDBACK = BASE_URL + "fhwl/user/userfeedback";

    /**
     * 399支付结果反馈
     */
    public static final String PAY_RESULT = BASE_URL + "fhwl/grade/buy-return";

    /*
     * 商品支付反馈结果
     * */
    public static final String SHOPS_PAY_RESULT = BASE_URL + "fhwl/order/svbill/return-goods";

    /*
     *
     * 线上商品支付
     * */
    public static final String PAY_SHOPS = BASE_URL + "fhwl/order/svbill/create-goods";

    /**
     * 删除银行卡
     */
    public static final String DELETE_UBC = BASE_URL + "fhwl/user/bankcard/deleteubc";

    /**
     * 新增银行卡信息
     */
    public static final String ADD_UBC = BASE_URL + "fhwl/user/bankcard/addubc";

    /**
     * 获取用户银行卡列表
     */
    public static final String GET_USER_BCLIST = BASE_URL + "fhwl/user/bankcard/getbclist";

    /**
     * 获取银行信息列表
     */
    public static final String GET_BANKLIST = BASE_URL + "fhwl/commons/bank/getbanklist";
    /**
     * 获取线下支付信息
     */
    public static final String GET_OFFLINE = BASE_URL + "fhwl/sysparam/pay-acc";

    /**
     * 为我买单
     */
    public static final String PAY_FOR_ME = BASE_URL + "fhwl/order/buybill/create";

    /**
     * 修改已驳回订单（为我买单）
     */
    public static final String UPDATE_ORDER = BASE_URL + "fhwl/order/buybill/update";

    /**
     * 为我买单记录
     */
    public static final String PAY_FOR_ME_RECORD = BASE_URL + "fhwl/order/buybill/list";

    /**
     * 查询默认策划推广比例
     */
    public static final String DEFAULT_PROPORTION = BASE_URL + "fhwl/sysparam/deft";

    /**
     * 提现
     */
    public static final String WITHDRAW = BASE_URL + "fhwl/withdrawal/create";

    /**
     * 提现手续费
     */
    public static final String WITHDRAW_FEE_RATE = BASE_URL + "fhwl/withdrawal/fee-rate";

    /**
     * 人气商家列表
     */
    public static final String GET_RMLIST = BASE_URL + "fhwl/merchant/getrmlist";

    /**
     * 黄花梨详情
     */
    public static final String HUANGHUALI_WEBVIEW = BASE_URL + "fhwl/yellowpear/get";

    /**
     * 购买黄花梨
     */
    public static final String HUANGHUALI_BUY = BASE_URL + "fhwl/yellowpear/buy";
    /**
     * 黄花梨购买记录
     */
    public static final String HUANGHUALI_LIST = BASE_URL + "fhwl/yellowpear/list";

    /**
     * 消息列表
     */
    public static final String NOTICE_LIST = BASE_URL + "fhwl/pushmsg/list";

    /**
     * 注册协议
     */
    public static final String H5_REGISTER_PROTOCOL = HOST_URL + "/static/protocol.html";
    /**
     * 帮助
     */
    public static final String H5_HELPER = HOST_URL + "/static/help.html";

    /**
     * 关于
     */
    public static final String H5_ABOUT_ME = HOST_URL + "/static/about.html";

}
