package com.feitianzhu.fu700.shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.DecimalFormat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.fu700.common.impl.onNetFinishLinstenerT;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.me.ui.ShopRecordDetailActivity;
import com.feitianzhu.fu700.me.ui.totalScore.TransferVoucherActivity;
import com.feitianzhu.fu700.model.PayInfo;
import com.feitianzhu.fu700.model.SelectPayNeedModel;
import com.feitianzhu.fu700.model.WXModel;
import com.feitianzhu.fu700.payforme.PayForMeEvent;
import com.feitianzhu.fu700.payforme.PayForMeRecordActivity;
import com.feitianzhu.fu700.shop.ui.ShopsPayActivity;
import com.feitianzhu.fu700.utils.PayUtils;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.utils.Urls;
import com.feitianzhu.fu700.view.AmountView;
import com.feitianzhu.fu700.vip.VipUpgradeActivity;
import com.socks.library.KLog;
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

/**
 * @class name：com.feitianzhu.fu700.shop
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/24 0024 下午 4:13
 * <p>
 * 线上和线下订单支付页面
 */
public class ShopPayActivity extends BaseActivity {
    public static final String IS_SHOW_ADDRESS = "is_show_address";
    public static final String PAY_AMOUNT = "pay_amount";

    @BindView(R.id.amount)
    TextView bottomAmount;
    @BindView(R.id.tv_amount)
    TextView topAmount;
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
    @BindView(R.id.amount_view)
    AmountView mAmountView;
    @BindView(R.id.count)
    TextView tvCount;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.edit_address)
    EditText editAddress;
    private String amount;
    private String totalAmount;
    private SelectPayNeedModel selectPayNeedModel;
    private String orderNo = "";//订单号
    private String str1;
    private String str2;
    private boolean isShow;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_pay;
    }

    @Override
    protected void onStart() {
        super.onStart();
        ShopDao.loadUserAuthImpl();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        selectPayNeedModel = new SelectPayNeedModel();
        titleName.setText("确认订单");
        weiXinIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
        /**
         * Spanned.SPAN_INCLUSIVE_EXCLUSIVE 从起始下标到终了下标，包括起始下标
         * Spanned.SPAN_INCLUSIVE_INCLUSIVE 从起始下标到终了下标，同时包括起始下标和终了下标
         * Spanned.SPAN_EXCLUSIVE_EXCLUSIVE 从起始下标到终了下标，但都不包括起始下标和终了下标
         * Spanned.SPAN_EXCLUSIVE_INCLUSIVE 从起始下标到终了下标，包括终了下标
         */
        str1 = "合计：";
        str2 = "¥ ";
        amount = String.format(Locale.getDefault(), "%.2f", getIntent().getDoubleExtra(PAY_AMOUNT, 0.00));
        totalAmount = amount;
        tvCount.setText("×1");
        topAmount.setText("");
        SpannableString span4 = new SpannableString(str2);
        SpannableString span5 = new SpannableString(amount);
        ForegroundColorSpan colorSpan4 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span4.setSpan(new AbsoluteSizeSpan(11, true), 0, span4.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span4.setSpan(colorSpan4, 0, span4.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan5 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span5.setSpan(new AbsoluteSizeSpan(18, true), 0, amount.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span5.setSpan(colorSpan5, 0, amount.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        topAmount.append(span4);
        topAmount.append(span5);
        setSpannableString(amount);

        isShow = getIntent().getBooleanExtra(IS_SHOW_ADDRESS, false);
        if (isShow) {
            llAddress.setVisibility(View.VISIBLE);
        } else {
            llAddress.setVisibility(View.GONE);
        }

        mAmountView = (AmountView) findViewById(R.id.amount_view);
        mAmountView.setGoods_storage(50);
        mAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onAmountChange(View view, int count) {
                tvCount.setText("×" + count);
                float aFloat = Float.parseFloat(amount);
                totalAmount = String.format(Locale.getDefault(), "%.2f", aFloat * count);
                setSpannableString(totalAmount);
            }
        });
    }

    @OnClick({R.id.tv_pay, R.id.weixinPay_icon, R.id.alipay_icon, R.id.balancePay_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_pay:
                if (isShow && TextUtils.isEmpty(editAddress.getText().toString().trim())) {
                    ToastUtils.showShortToast("请输入收货地址");
                    return;
                }
                ToastUtils.showShortToast("待开发");
                /*ShopHelp.veriPassword(this, new onConnectionFinishLinstener() {
                    @Override
                    public void onSuccess(int code, Object result) {
                        String passowrd = (String) result;
                        pay(passowrd);
                    }

                    @Override
                    public void onFail(int code, String result) {
                        ToastUtils.showShortToast(result);
                    }
                });*/
                break;
            case R.id.weixinPay_icon:
                weiXinIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
                alipayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                balancePayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                selectPayNeedModel.setPayChannel("wx");
                break;
            case R.id.alipay_icon:
                weiXinIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                alipayIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
                balancePayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                selectPayNeedModel.setPayChannel("alipay");
                break;
            case R.id.balancePay_icon:
                weiXinIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                alipayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                balancePayIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
                selectPayNeedModel.setPayChannel("");
                break;
        }
    }

    private void pay(String mPassowrd) {
        String merchantid = "2";
        if ("wx".equals(selectPayNeedModel.getPayChannel())) {
            showloadDialog("");
            ShopDao.postShopWxPay(merchantid, totalAmount, selectPayNeedModel.getPayChannel(), mPassowrd,
                    new onNetFinishLinstenerT<WXModel>() {
                        @Override
                        public void onSuccess(int code, WXModel result) {
                            Constant.PayFlag = PayInfo.ShopPay;
                            IWXAPI api = WXAPIFactory.createWXAPI(ShopPayActivity.this, result.appid);
                            api.registerApp(result.appid);
                            PayReq mPayReq = new PayReq();
                            orderNo = result.orderNo;
                            Constant.WX_APP_ID = result.appid + "";
                            mPayReq.appId = result.appid;
                            mPayReq.partnerId = result.partnerid;
                            mPayReq.prepayId = result.prepayid;
                            mPayReq.packageValue = "Sign=WXPay";
                            mPayReq.nonceStr = result.noncestr;
                            mPayReq.timeStamp = result.timestamp + "";
                            mPayReq.sign = result.sign;
                            api.sendReq(mPayReq);
                            goneloadDialog();
                            ToastUtils.showShortToast("正在打开微信中");
                        }

                        @Override
                        public void onFail(int code, String result) {
                            goneloadDialog();
                            ToastUtils.showShortToast(result);
                        }
                    });
        } else {
            ShopDao.postShopPay(merchantid, totalAmount, selectPayNeedModel.getPayChannel(), mPassowrd,
                    new onConnectionFinishLinstener() {
                        @Override
                        public void onSuccess(int code, Object result) {
                            if ("alipay".equals(selectPayNeedModel.getPayChannel())) {
                                String orderInfo = result.toString();
                                aliPay(orderInfo, orderNo);
                            }
                        }

                        @Override
                        public void onFail(int code, String result) {
                            ToastUtils.showShortToast(result);
                        }
                    });
        }

    }

    private void aliPay(String orderInfo, String orderNo) {

        PayUtils.aliPay(ShopPayActivity.this, orderInfo, new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                ToastUtils.showShortToast("支付成功");
                selectPayNeedModel.setIsPay("1");
                //payResult(selectPayNeedModel, orderNo);
                finish();
            }

            @Override
            public void onFail(int code, String result) {
                selectPayNeedModel.setIsPay("0");
                //payResult(selectPayNeedModel, orderNo);
                ToastUtils.showShortToast("支付失败");
            }
        });
    }

    /*
     * 将支付结果反馈给后台
     * */
    public void payResult(SelectPayNeedModel mSelectModel, String orderNo) {
        /*
        * int userId,int gradeId,String provinceId,String provinceName,String cityId,String cityName,String areaId,
           String areaName,String payChannel,String isPay
        * */
        OkHttpUtils
                .post()
                .url(Urls.PAY_RESULT)
                .addParams("accessToken", Constant.ACCESS_TOKEN)
                .addParams("userId", Constant.LOGIN_USERID)
                .addParams("gradeId", "")
                .addParams("provinceId", "")
                .addParams("provinceName", "")
                .addParams("cityId", "")
                .addParams("cityName", "")
                .addParams("areaId", "")
                .addParams("areaName", "")
                .addParams("payChannel", mSelectModel.getPayChannel())
                .addParams("isPay", mSelectModel.isPay)
                .addParams("orderNo", orderNo)
                .build()
                .execute(new Callback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(Object response, int id) {

                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPayMessageCall(PayInfo msg) {
        switch (msg.getCurrentInfo()) {
            case PayInfo.ShopPay:
                if (msg.getIsSuccess() == PayInfo.SUCCESS) {
                    selectPayNeedModel.setIsPay("1");
                    // payResult(selectPayNeedModel, orderNo);
                    finish();
                } else {
                    selectPayNeedModel.setIsPay("0");
                    // payResult(selectPayNeedModel, orderNo);
                }

                break;
        }
    }

    @Override
    protected void initData() {

    }

    @SuppressLint("SetTextI18n")
    public void setSpannableString(String str3) {
        tvTotalAmount.setText("￥" + str3);
        bottomAmount.setText("");
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

        bottomAmount.append(span1);
        bottomAmount.append(span2);
        bottomAmount.append(span3);

    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }
}
