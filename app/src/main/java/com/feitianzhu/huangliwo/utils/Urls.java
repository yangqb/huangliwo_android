package com.feitianzhu.huangliwo.utils;

/**
 * Created by Lee on 2017/9/3.
 */

public class Urls {


    //private static final String BASE_URL = "http://www.huangliwo.top:8088/"; //正式环境
    private static final String BASE_URL = "http://39.106.65.35:8088/"; //测试地址
    //private static final String BASE_URL = "http://192.168.0.21:8089/";//钟工本地地址
    //private static final String BASE_URL = "http://192.168.0.15:8089/"; //周工本地地址
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

    /*
     * 首页公告
     * */
    public static final String GET_HOME_NOTICE = BASE_URL + "/fhwl/pushmsg/getPushMsg";
    /*
     * 首页活动弹窗
     * */
    public static final String GET_POP_DATA = BASE_URL + "/fhwl/index/getPopup";

    /**
     * 获取首页数据
     */
    public static final String GET_INDEX = BASE_URL + "fhwl/index/getindex";

    /*
     * 年货专区数据
     * */
    public static final String GET_NEW_YEAR = BASE_URL + "/fhwl/shop/getactivity";
    /*
     * 获取VIP赠送礼品
     * */
    public static final String GET_VIP_PRESENT = BASE_URL + "fhwl/shopGift/getShopGiftList";

    /*
     * 首页商品数据
     * */
    public static final String GET_HOME_GOODS_LIST = BASE_URL + "fhwl/index/pageGoods";

    /*
     * 商品搜索
     * */

    public static final String GET_SEARCH_LIST = BASE_URL + "fhwl/index/search";

    /*
     * 获取商城分类
     * */
    public static final String GET_SHOP_CLASS = BASE_URL + "fhwl/shop/getshop";

    /*
     * 商城分类的商品
     * */
    public static final String GET_SHOP = BASE_URL + "fhwl/shop/getclsid";

    /*
     * 获取分类商铺列表
     * */
    public static final String GET_MERCHANTS = BASE_URL + "fhwl/merchant/getMerchantByCls";

    /*
     * 商品详情数据
     * */
    public static final String GET_SHOP_DETAIL = BASE_URL + "fhwl/index/getGoodsDetail";

    /*
     * 商品规格参数
     * */
    public static final String GET_PRODUCT_PARAMETERS = BASE_URL + "fhwl/shop/getskuValue";

    /*
     * 订单分类的数量
     * */
    public static final String GTE_ORDER_COUNT = BASE_URL + "fhwl/order/getBuyInfo";
    /*
     * 所有商品订单列表数据status --- -1代表全部,0代表待评价-2代表售后的单子（退款中和已退款） 其他的按照订单的状态传值
     * */
    public static final String GET_ORDER_INFO = BASE_URL + "fhwl/order/svbill/goodsorder";
    /*
     *订单详情页
     * */
    public static final String GET_ORDER_DETAIL = BASE_URL + "fhwl/order/svbill/orderDetail";
    /*
     * 删除订单
     * */
    public static final String DELETE_ORDER = BASE_URL + "fhwl/order/delete";

    /*
     * 取消订单
     * */
    public static final String CANCEL_ORDER = BASE_URL + "fhwl/order/cancelOrder";
    /*
     * 退款
     * */
    public static final String REFUND_ORDER = BASE_URL + "fhwl/order/refundOrder";
    /*
     * 确认收货
     * */
    public static final String CONFIRM_ORDER = BASE_URL + "fhwl/order/confirmReceipt";
    /*
     * 查看物流
     * */
    public static final String GET_LOGISTICS_INFO = BASE_URL + "fhwl/express/getExpressByNo";

    /*
     * 订单评价POST
     * */
    public static final String EVALUATE_ORDER = BASE_URL + "fhwl/order/evaluate";
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

    /*
     * 获取余额信息
     * */
    public static final String GET_USER_MONEY_INFO = BASE_URL + "fhwl/user/getUserMoneyInfo";
    /*
     * 余额细则,细则类型 1推广返利 2消费折扣
     * */
    public static final String GET_DETAIL_RULES = BASE_URL + "fhwl/order/detailRules";

    /*
     * 创建商铺
     * */
    public static final String CREATE_MERCHANTS = BASE_URL + "fhwl/merchant/createMerchant";
    /*
     * 编辑商铺
     * */
    public static final String UPDATA_MERCHANTS = BASE_URL + "fhwl/merchant/updateMerchant";

    /*
     * 商铺详情数据
     * */
    public static final String GET_MERCHANTS_DETAIL = BASE_URL + "fhwl/merchant/getMerchantInfo";

    /*
     * 获取我的商铺列表
     * */
    public static final String GET_MERCHANTS_LIST = BASE_URL + "fhwl/merchant/getMerchants";

    /*
     * 获取商户收款二维码
     * */
    public static final String GET_MERCHANTS_PAYMENT_CODE = BASE_URL + "fhwl/qrcode/merchant/create";


    /*
     * 商铺收益细则和订单列表
     * */
    public static final String GET_EARNINGS_RULES = BASE_URL + "fhwl/merchant/getMerchantOrder";

    /*
     * 提现记录
     * */
    public static final String GET_WITHDRAW_RECORD = BASE_URL + "fhwl/withdrawal/selectWithdrawal";

    /*
     * 取消提现
     * */
    public static final String WITHDRAW_CANCEL = BASE_URL + "fhwl/withdrawal/cancel";

    /*
     * 绑定支付宝账号
     * */
    public static final String BING_ALI_ACCOUNT = BASE_URL + "fhwl/user/bankcard/addAliAccount";

    /*
     * 获取支付宝账号
     * */
    public static final String GET_ALI_ACCOUNT = BASE_URL + "fhwl/user/bankcard/getAliAccount";

    /*
     * 新增套餐
     * */
    public static final String ADD_SETMEAL = BASE_URL + "fhwl/setMeal/saveSetMeal";

    /*
     * 修改套餐
     * */
    public static final String UPDATE_SETMEAL = BASE_URL + "fhwl/setMeal/updateSetMeal";

    /*
     * 获取套餐列表
     * */
    public static final String GET_SETMEAL_LIST = BASE_URL + "fhwl/setMeal/getSetMealList";

    /*
     * 获取所有套餐的评价
     * */
    public static final String GET_SETMEAL_EVALIST = BASE_URL + "fhwl/merchant/getEvalList";

    /*
     * 获取套餐详情
     * */
    public static final String GET_SETMEAL_DETAIL = BASE_URL + "fhwl/setMeal/getSetMeal";

    /*
     * 商家录单
     * */
    public static final String RECORD_ORDER = BASE_URL + "fhwl/merchantPay/record";

    /*
     * 套餐上下架
     * */
    public static final String UPDATE_SETMEAL_SHELF = BASE_URL + "fhwl/setMeal/updateShelf";

    /*
     * 获取我推荐的商铺列表
     * */
    public static final String GET_PUSH_MERCHANTS_LIST = BASE_URL + "fhwl/merchant/recommendDetail";

    /**
     * 获取商铺类型
     */
    public final static String GET_MERCHANTS_TYPE = BASE_URL + "fhwl/commons/merchant/getmclslist";

    /*
     * 余额提现
     * */
    public static final String WITHDRAWAL = BASE_URL + "fhwl/withdrawal/create";

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
     * 线上商品支付生成订单
     * */
    public static final String PAY_SHOPS = BASE_URL + "fhwl/order/svbill/create-goods";

    /*
     * 线上套餐支付生成订单
     * */
    public static final String PAY_SETMEAL = BASE_URL + "fhwl/merchantPay/buySetMeal";

    /*
     * 商户线下支付
     * */
    public static final String RECEIVABLES_PAY_SETMEAL = BASE_URL + "fhwl/merchantPay/receivables";

    /*
     * 套餐订单详情
     * */
    public static final String SETMEAL_ORDER_DETAIL = BASE_URL + "fhwl/merchantPay/orderDetail";

    /*
     * 套餐订单列表
     * */
    public static final String SETMEAL_ORDER_LIST = BASE_URL + "fhwl/merchantPay/list";
    /*
     * 取消套餐订单和退款
     * */
    public static final String CANCEL_SETMEAL_ORDER = BASE_URL + "fhwl/merchantPay/updateOrder";
    /*
     * 套餐评价
     * */
    public static final String EVALUATE_SETMEAL_ORDER = BASE_URL + "fhwl/merchantPay/eval";

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
    public static final String H5_REGISTER_PROTOCOL = "/static/protocol.html";
    /**
     * 帮助
     */
    public static final String H5_HELPER = "/static/help.html";

    /**
     * 关于
     */
    public static final String H5_ABOUT_ME = "/static/about.html";

}
