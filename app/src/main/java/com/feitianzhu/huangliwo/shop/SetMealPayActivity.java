package com.feitianzhu.huangliwo.shop;

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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.model.PayInfo;
import com.feitianzhu.huangliwo.model.PayModel;
import com.feitianzhu.huangliwo.model.SetMealPayInfo;
import com.feitianzhu.huangliwo.pushshop.bean.SetMealInfo;
import com.feitianzhu.huangliwo.utils.PayUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.utils.doubleclick.SingleClick;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * package name: com.feitianzhu.huangliwo.shop
 * user: yangqinbo
 * date: 2020/1/15
 * time: 17:16
 * email: 694125155@qq.com
 */
public class SetMealPayActivity extends BaseActivity {
    private SetMealInfo setMealInfo;
    public static final String SETMEAL_ORDERI_NFO = "setmeal_orderi_nfo";
    public static final String ORDER_NO = "order_no";
    private String str1 = "合计：";
    private String str2 = "¥ ";
    private String payChannel = "wx";
    private String appId = "";
    private String token;
    private String userId;
    private String orderNo = "";//订单号
    private String alreadyOrderNo = "";
    private String orderInfo = "";
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.name)
    TextView tvName;
    @BindView(R.id.summary)
    TextView tvSummary;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.pay_amount)
    TextView payAmount;
    @BindView(R.id.weixinPay_icon)
    ImageView weiXinIcon;
    @BindView(R.id.alipay_icon)
    ImageView alipayIcon;
    @BindView(R.id.balancePay_icon)
    ImageView balancePayIcon;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_setmeal_pay;
    }

    @Override
    protected void initView() {
        titleName.setText("确认订单");
        EventBus.getDefault().register(this);
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        weiXinIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
        setMealInfo = (SetMealInfo) getIntent().getSerializableExtra(SETMEAL_ORDERI_NFO);
        alreadyOrderNo = getIntent().getStringExtra(ORDER_NO);
        if (setMealInfo != null) {
            String[] imgUrls = setMealInfo.getImgs().split(",");
            Glide.with(this).load(imgUrls[0])
                    .apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into(imageView);

            tvName.setText(setMealInfo.getSmName());
            tvSummary.setText(setMealInfo.getRemark());
            String price = String.format(Locale.getDefault(), "%.2f", setMealInfo.getPrice());
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

            setSpannableString(price);
        }

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.tv_pay, R.id.weixinPay_icon, R.id.alipay_icon, R.id.balancePay_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.tv_pay:
                pay();
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
    public void pay() {
        if (payChannel.equals("wx")) {
            appId = Constant.WX_APP_ID;
        } else {
            appId = "";
        }
        String json;
        SetMealPayInfo info = new SetMealPayInfo();
        if ("".equals(alreadyOrderNo)) {//详情页过来的
            info.setChannel(payChannel); //支付渠道支付渠道（wx：微信，alipay：支付宝，balance：余额
            info.setMerchantId(setMealInfo.getMerchantId());
            info.setSmId(setMealInfo.getSmId());
            info.setUserId(userId);
            info.setAmount(setMealInfo.getPrice());
            json = new Gson().toJson(info);
        } else { //订单页过来的
            info.setChannel(payChannel);
            info.setOrderNo(alreadyOrderNo);
            info.setSmId(setMealInfo.getSmId());
            info.setUserId(setMealInfo.getUserId());
            info.setMerchantId(setMealInfo.getMerchantId());
            json = new Gson().toJson(info);
        }

        OkGo.<LzyResponse<PayModel>>post(Urls.PAY_SETMEAL)
                .tag(this)
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)//
                .params("appId", appId)  //这个是微信才需要的，
                .params("merchantOrderInfo", json)
                .execute(new JsonCallback<LzyResponse<PayModel>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<PayModel>> response) {
                        super.onSuccess(SetMealPayActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {

                                if ("wx".equals(payChannel)) {
                                    orderNo = response.body().data.orderNo;
                                    wexinPay(response.body().data);
                                } else if ("alipay".equals(payChannel)) {
                                    orderInfo = response.body().data.payParam;
                                    orderNo = response.body().data.orderNo;
                                    aliPay(orderInfo, orderNo);
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
        Constant.PayFlag = PayInfo.SETMEAL_PAY;
        IWXAPI api = WXAPIFactory.createWXAPI(SetMealPayActivity.this, result.appid);
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
        ToastUtils.show("正在打开微信中");
    }


    private void aliPay(String payProof, String orderNo) {

        PayUtils.aliPay(SetMealPayActivity.this, payProof, new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                ToastUtils.show("支付成功");
                Intent intent = new Intent(SetMealPayActivity.this, SetMealOrderDetailActivity.class);
                intent.putExtra(SetMealOrderDetailActivity.ORDER_NO, orderNo);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFail(int code, String result) {
                ToastUtils.show("支付失败");
                if ("".equals(alreadyOrderNo)) {
                    //跳到订单详情
                    Intent intent = new Intent(SetMealPayActivity.this, SetMealOrderDetailActivity.class);
                    intent.putExtra(SetMealOrderDetailActivity.ORDER_NO, orderNo);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPayMessageCall(PayInfo msg) {
        if (msg.getCurrentInfo() == PayInfo.SETMEAL_PAY) {
            if (msg.getIsSuccess() == PayInfo.SUCCESS) {
                ToastUtils.show("支付成功");
                Intent intent = new Intent(SetMealPayActivity.this, SetMealOrderDetailActivity.class);
                intent.putExtra(SetMealOrderDetailActivity.ORDER_NO, orderNo);
                startActivity(intent);
                finish();
            } else {
                ToastUtils.show("支付失败");
                if ("".equals(alreadyOrderNo)) {
                    //跳到订单详情
                    Intent intent = new Intent(SetMealPayActivity.this, SetMealOrderDetailActivity.class);
                    intent.putExtra(SetMealOrderDetailActivity.ORDER_NO, orderNo);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void setSpannableString(String str3) {
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
