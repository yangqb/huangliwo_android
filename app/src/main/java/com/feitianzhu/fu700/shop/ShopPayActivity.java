package com.feitianzhu.fu700.shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.fu700.me.AddressManagementActivity;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.model.AddressInfo;
import com.feitianzhu.fu700.model.BaseGoodsListBean;
import com.feitianzhu.fu700.model.GoodsOrderInfo;
import com.feitianzhu.fu700.model.PayInfo;
import com.feitianzhu.fu700.model.WXModel;
import com.feitianzhu.fu700.shop.ui.OrderDetailActivity;
import com.feitianzhu.fu700.utils.PayUtils;
import com.feitianzhu.fu700.utils.SPUtils;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.utils.Urls;
import com.feitianzhu.fu700.utils.doubleclick.SingleClick;
import com.feitianzhu.fu700.view.AmountView;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.feitianzhu.fu700.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.fu700.common.Constant.USERID;

/**
 * @class name：com.feitianzhu.fu700.shop
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/24 0024 下午 4:13
 * <p>
 * 线上和线下订单支付页面
 */
public class ShopPayActivity extends BaseActivity {
    public static final String GOODS_VALUE_ID = "goods_value_id";
    public static final String IS_SHOW_ADDRESS = "is_show_address";
    public static final String PAY_DATA = "pay_data";
    private static final int REQUEST_CODE = 1000;
    private AddressInfo.ShopAddressListBean addressBean;
    private List<AddressInfo.ShopAddressListBean> addressInfos = new ArrayList<>();
    @BindView(R.id.pay_amount)
    TextView payAmount;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.weixinPay_icon)
    ImageView weiXinIcon;
    @BindView(R.id.alipay_icon)
    ImageView alipayIcon;
    @BindView(R.id.balancePay_icon)
    ImageView balancePayIcon;
    @BindView(R.id.total_amount)
    TextView tvTotalAmount;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    @BindView(R.id.no_address)
    LinearLayout noAddress;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.amount_view)
    AmountView mAmountView;
    @BindView(R.id.name)
    TextView tvName;
    @BindView(R.id.summary)
    TextView tvSummary;
    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.consignee_name)
    TextView consigneeName;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.count)
    TextView tvCount;
    @BindView(R.id.postage)
    TextView postage;

    private String price;
    private String totalAmount;
    private String orderNo = "";//订单号
    private String orderInfo = "";
    private String str1 = "合计：";
    private String str2 = "¥ ";
    private boolean isShow;
    private boolean isAddress;
    private BaseGoodsListBean goodsListBean;
    private GoodsOrderInfo.GoodsOrderListBean orderListBean = new GoodsOrderInfo.GoodsOrderListBean();
    private int shopCount = 1; //购买数量
    private String valueId;
    private String payChannel = "wx";
    private String appId = "";
    private String token;
    private String userId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_pay;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        valueId = getIntent().getStringExtra(GOODS_VALUE_ID);
        goodsListBean = (BaseGoodsListBean) getIntent().getSerializableExtra(PAY_DATA);
        titleName.setText("确认订单");
        tvPrice.setText("");
        weiXinIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
        /**
         * Spanned.SPAN_INCLUSIVE_EXCLUSIVE 从起始下标到终了下标，包括起始下标
         * Spanned.SPAN_INCLUSIVE_INCLUSIVE 从起始下标到终了下标，同时包括起始下标和终了下标
         * Spanned.SPAN_EXCLUSIVE_EXCLUSIVE 从起始下标到终了下标，但都不包括起始下标和终了下标
         * Spanned.SPAN_EXCLUSIVE_INCLUSIVE 从起始下标到终了下标，包括终了下标
         */
        if (goodsListBean != null) {
            price = String.format(Locale.getDefault(), "%.2f", goodsListBean.getPrice());
            tvName.setText(goodsListBean.getGoodsName());
            tvSummary.setText(goodsListBean.getSummary());
            postage.setText("¥" + String.format(Locale.getDefault(), "%.2f", goodsListBean.getPostage()));
            Glide.with(this).load(goodsListBean.getGoodsImg())
                    .apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(imageView);
            setSpannableString(String.format(Locale.getDefault(), "%.2f", goodsListBean.getPrice() + goodsListBean.getPostage()));
        } else {
            price = String.format(Locale.getDefault(), "%.2f", 0.00);
            setSpannableString(price);
        }

        totalAmount = price;
        SpannableString span4 = new SpannableString(str2);
        SpannableString span5 = new SpannableString(price);
        ForegroundColorSpan colorSpan4 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span4.setSpan(new AbsoluteSizeSpan(11, true), 0, span4.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span4.setSpan(colorSpan4, 0, span4.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan5 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span5.setSpan(new AbsoluteSizeSpan(18, true), 0, span5.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span5.setSpan(colorSpan5, 0, span5.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        tvPrice.append(span4);
        tvPrice.append(span5);

        isShow = getIntent().getBooleanExtra(IS_SHOW_ADDRESS, false);
        if (isShow) {
            //是否有默认地址
            getAddress();
        } else {
            noAddress.setVisibility(View.GONE);
            rlAddress.setVisibility(View.GONE);
        }

        mAmountView = (AmountView) findViewById(R.id.amount_view);
        mAmountView.setGoods_storage(50);
        mAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onAmountChange(View view, int count) {
                shopCount = count;
                tvCount.setText("×" + count);
                float aFloat = Float.parseFloat(price);
                totalAmount = String.format(Locale.getDefault(), "%.2f", aFloat * count + goodsListBean.getPostage());
                setSpannableString(totalAmount);
            }
        });
    }

    @SingleClick
    @OnClick({R.id.left_button, R.id.tv_pay, R.id.weixinPay_icon, R.id.alipay_icon, R.id.balancePay_icon, R.id.rl_address, R.id.no_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_pay:
                if (isShow && !isAddress) {
                    ToastUtils.showShortToast("请添加收货地址");
                    return;
                }
                pay2();
                break;
            case R.id.weixinPay_icon:
                weiXinIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
                alipayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                balancePayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                payChannel = "wx";
                break;
            case R.id.alipay_icon:
                weiXinIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                alipayIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
                balancePayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                payChannel = "alipay";
                break;
            case R.id.balancePay_icon:
                weiXinIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                alipayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                balancePayIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
                payChannel = "balance";
                break;
            case R.id.left_button:
                finish();
                break;
            case R.id.no_address:
            case R.id.rl_address:
                Intent intent = new Intent(ShopPayActivity.this, AddressManagementActivity.class);
                intent.putExtra(AddressManagementActivity.IS_SELECT, true);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }


    /*
     * 生成订单
     * */
    private void pay2() {
        if (payChannel.equals("wx")) {
            appId = Constant.WX_APP_ID;
        } else {
            appId = "";
        }
        orderListBean.setChannel(payChannel); //支付渠道支付渠道（wx：微信，alipay：支付宝，balance：余额
        orderListBean.setAddressId(addressBean.getAddressId() + "");
        orderListBean.setGoodId(goodsListBean.getGoodsId());
        orderListBean.setUserId(Integer.valueOf(userId));
        orderListBean.setGoodsQty(shopCount);   //数量
        if (valueId != null) {
            orderListBean.setValueId(valueId);  //规格ID
        }
        orderListBean.setOrderNo(""); //详情页的无orderNo
        String json = new Gson().toJson(orderListBean);
        OkHttpUtils.post()
                .url(Urls.PAY_SHOPS)
                .addParams(ACCESSTOKEN, token)//
                .addParams(USERID, userId)//
                .addParams("appId", appId)  //这个是微信才需要的，
                .addParams("order", json)
                .build()
                .execute(new Callback() {

                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return mData;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("提交订单失败");
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        try {
                            JSONObject object = new JSONObject(response.toString());
                            if ("wx".equals(payChannel)) {
                                WXModel wxModel = new Gson().fromJson(object.toString(), WXModel.class);
                                orderNo = wxModel.orderNo;
                                wexinPay(wxModel);
                            } else if ("alipay".equals(payChannel)) {
                                orderInfo = object.getString("payParam");
                                orderNo = object.getString("orderNo");
                                aliPay(orderInfo, orderNo);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void wexinPay(WXModel result) {
        Constant.PayFlag = PayInfo.ShopPay;
        IWXAPI api = WXAPIFactory.createWXAPI(ShopPayActivity.this, result.appid);
        api.registerApp(Constant.WX_APP_ID);
        PayReq mPayReq = new PayReq();
        mPayReq.appId = Constant.WX_APP_ID;
        mPayReq.partnerId = result.partnerid;
        mPayReq.prepayId = result.prepayid;
        mPayReq.packageValue = "Sign=WXPay";
        mPayReq.nonceStr = result.noncestr;
        mPayReq.timeStamp = result.timestamp + "";
        mPayReq.sign = result.sign;
        api.sendReq(mPayReq);
        ToastUtils.showShortToast("正在打开微信中");
    }


    private void aliPay(String payProof, String orderNo) {

        PayUtils.aliPay(ShopPayActivity.this, payProof, new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                ToastUtils.showShortToast("支付成功");
                finish();
            }

            @Override
            public void onFail(int code, String result) {
                ToastUtils.showShortToast("支付失败");
                //跳到订单详情
                Intent intent = new Intent(ShopPayActivity.this, OrderDetailActivity.class);
                intent.putExtra(OrderDetailActivity.ORDER_NO, orderNo);
                startActivity(intent);
                finish();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPayMessageCall(PayInfo msg) {
        if (msg.getCurrentInfo() == PayInfo.ShopPay) {
            if (msg.getIsSuccess() == PayInfo.SUCCESS) {
                ToastUtils.showShortToast("支付成功");
                finish();
            } else {
                ToastUtils.showShortToast("支付失败");
                //跳到订单详情
                Intent intent = new Intent(ShopPayActivity.this, OrderDetailActivity.class);
                intent.putExtra(OrderDetailActivity.ORDER_NO, orderNo);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        getAddress();
    }

    /*
     * 获取默认收货地址
     * */
    public void getAddress() {
        OkHttpUtils.post()
                .url(Urls.GET_ADDRESS)
                .addParams("accessToken", token)
                .addParams("userId", userId)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return new Gson().fromJson(mData, AddressInfo.class);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        AddressInfo addressInfo = (AddressInfo) response;
                        addressInfos = addressInfo.getShopAddressList();
                        if (addressInfos.size() > 0) {
                            for (AddressInfo.ShopAddressListBean address : addressInfos
                            ) {
                                if (address.getIsDefalt() == 1) {
                                    noAddress.setVisibility(View.GONE);
                                    rlAddress.setVisibility(View.VISIBLE);
                                    addressBean = address;
                                    tvAddress.setText(addressBean.getProvinceName() + addressBean.getCityName() + addressBean.getAreaName() + addressBean.getDetailAddress());
                                    consigneeName.setText(addressBean.getUserName());
                                    phone.setText(addressBean.getPhone());
                                    isAddress = true;
                                    break;
                                } else {
                                    noAddress.setVisibility(View.VISIBLE);
                                    rlAddress.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            noAddress.setVisibility(View.VISIBLE);
                            rlAddress.setVisibility(View.GONE);
                        }

                    }
                });
    }

    @SuppressLint("SetTextI18n")
    public void setSpannableString(String str3) {
        tvTotalAmount.setText("¥" + str3);
        payAmount.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span2 = new SpannableString(str2);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#666666"));
        span1.setSpan(new AbsoluteSizeSpan(13, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span2.setSpan(new AbsoluteSizeSpan(11, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(new StyleSpan(Typeface.BOLD), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(colorSpan2, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span3.setSpan(new AbsoluteSizeSpan(20, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        payAmount.append(span1);
        payAmount.append(span2);
        payAmount.append(span3);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                addressBean = (AddressInfo.ShopAddressListBean) data.getSerializableExtra(AddressManagementActivity.ADDRESS_DATA);
                if (addressBean != null) {
                    noAddress.setVisibility(View.GONE);
                    rlAddress.setVisibility(View.VISIBLE);
                    tvAddress.setText(addressBean.getProvinceName() + addressBean.getCityName() + addressBean.getAreaName() + addressBean.getDetailAddress());
                    consigneeName.setText(addressBean.getUserName());
                    phone.setText(addressBean.getPhone());
                    isAddress = true;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
