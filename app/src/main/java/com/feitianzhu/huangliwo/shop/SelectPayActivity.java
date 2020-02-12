package com.feitianzhu.huangliwo.shop;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.GoodsOrderInfo;
import com.feitianzhu.huangliwo.model.PayInfo;
import com.feitianzhu.huangliwo.model.PayModel;
import com.feitianzhu.huangliwo.model.WXModel;
import com.feitianzhu.huangliwo.utils.PayUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
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

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * package name: com.feitianzhu.fu700.shop
 * user: yangqinbo
 * date: 2019/12/20
 * time: 18:42
 * email: 694125155@qq.com
 */
public class SelectPayActivity extends BaseActivity {
    public static final String ORDER_DATA = "order_data";
    private GoodsOrderInfo.GoodsOrderListBean goodsOrderBean;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.summary)
    TextView tvSummary;
    @BindView(R.id.name)
    TextView tvName;
    @BindView(R.id.consignee_name)
    TextView consigneeName;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.total_amount)
    TextView tvTotalAmount;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.pay_amount)
    TextView payAmount;
    @BindView(R.id.count)
    TextView tvCount;
    @BindView(R.id.weixinPay_icon)
    ImageView weiXinIcon;
    @BindView(R.id.alipay_icon)
    ImageView alipayIcon;
    @BindView(R.id.balancePay_icon)
    ImageView balancePayIcon;
    @BindView(R.id.postage)
    TextView postage;
    private String str1 = "合计：";
    private String str2 = "¥ ";
    private String payChannel = "wx";
    private String appId = "";
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    private String token;
    private String userId;
    private GoodsOrderInfo.GoodsOrderListBean orderInfo = new GoodsOrderInfo.GoodsOrderListBean();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_pay2;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        titleName.setText("确认支付");
        tvPrice.setText("");
        goodsOrderBean = (GoodsOrderInfo.GoodsOrderListBean) getIntent().getSerializableExtra(ORDER_DATA);
        weiXinIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
        if (goodsOrderBean != null) {
            setSpannableString(String.format(Locale.getDefault(), "%.2f", goodsOrderBean.getAmount() + goodsOrderBean.getPostage()));
            tvCount.setText("×" + goodsOrderBean.getCount());
            tvName.setText(goodsOrderBean.getShopName());
            tvSummary.setText(goodsOrderBean.getGoodName());
            postage.setText("¥" + String.format(Locale.getDefault(), "%.2f", goodsOrderBean.getPostage()));
            Glide.with(this).load(goodsOrderBean.getGoodsImg())
                    .apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(imageView);
            if (goodsOrderBean.getDetailAddress() != null && !TextUtils.isEmpty(goodsOrderBean.getDetailAddress())) {
                rlAddress.setVisibility(View.VISIBLE);
                tvAddress.setText(goodsOrderBean.getDetailAddress());
            } else {
                rlAddress.setVisibility(View.GONE);
            }

            consigneeName.setText(goodsOrderBean.getBuyerName());
            phone.setText(goodsOrderBean.getBuyerPhone());
        } else {
            setSpannableString(String.format(Locale.getDefault(), "%.2f", 0.00));
        }
        SpannableString span4 = new SpannableString(str2);
        SpannableString span5 = new SpannableString(String.format(Locale.getDefault(), "%.2f", goodsOrderBean.getPrice()));
        ForegroundColorSpan colorSpan4 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span4.setSpan(new AbsoluteSizeSpan(11, true), 0, span4.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span4.setSpan(colorSpan4, 0, span4.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan5 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span5.setSpan(new AbsoluteSizeSpan(18, true), 0, span5.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span5.setSpan(colorSpan5, 0, span5.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        tvPrice.append(span4);
        tvPrice.append(span5);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.left_button, R.id.tv_pay, R.id.weixinPay_icon, R.id.alipay_icon, R.id.balancePay_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_pay:
                pay2();
                break;
            case R.id.left_button:
                finish();
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
        orderInfo.setChannel(payChannel);
        orderInfo.setOrderNo(goodsOrderBean.getOrderNo());
        String json = new Gson().toJson(orderInfo);

        OkGo.<LzyResponse<PayModel>>post(Urls.PAY_SHOPS)
                .tag(this)
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)//
                .params("appId", appId)  //这个是微信才需要的，
                .params("order", json)
                .execute(new JsonCallback<LzyResponse<PayModel>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<PayModel>> response) {
                        super.onSuccess(SelectPayActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {
                                if ("wx".equals(payChannel)) {
                                    wexinPay(response.body().data);
                                } else if ("alipay".equals(payChannel)) {
                                    String orderInfo = response.body().data.payParam;
                                    aliPay(orderInfo);
                                }
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<PayModel>> response) {
                        super.onError(response);
                    }
                });
    }


    private void wexinPay(PayModel result) {
        Constant.PayFlag = PayInfo.ShopPay;
        IWXAPI api = WXAPIFactory.createWXAPI(SelectPayActivity.this, result.appid);
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

    private void aliPay(String orderInfo) {
        PayUtils.aliPay(SelectPayActivity.this, orderInfo, new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                ToastUtils.showShortToast("支付成功");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFail(int code, String result) {
                ToastUtils.showShortToast("支付失败");
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPayMessageCall(PayInfo msg) {
        if (msg.getCurrentInfo() == PayInfo.ShopPay) {
            if (msg.getIsSuccess() == PayInfo.SUCCESS) {
                ToastUtils.showShortToast("支付成功");
                setResult(RESULT_OK);
                finish();
            } else {
                ToastUtils.showShortToast("支付失败");
            }
        }
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
