package com.feitianzhu.huangliwo.pushshop;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.PayInfo;
import com.feitianzhu.huangliwo.model.WXModel;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.pushshop.bean.PaymentInfo;
import com.feitianzhu.huangliwo.utils.EditTextUtils;
import com.feitianzhu.huangliwo.utils.PayUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.google.gson.Gson;
import com.itheima.roundedimageview.RoundedImageView;
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

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * package name: com.feitianzhu.huangliwo.pushshop
 * user: yangqinbo
 * date: 2020/1/18
 * time: 13:51
 * email: 694125155@qq.com
 */
public class MyPaymentActivity extends BaseActivity {
    public static final String PAYMENT_INFO = "payment_info";
    private MerchantsModel merchantsBean;
    private String payInfo;
    private String payChannel = "wx";
    private String appId = "";
    private PaymentInfo paymentInfo;
    private String orderNo = "";//订单号
    private String orderInfo = "";
    private String token;
    private String userId;
    private String merchantsId;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.weixinPay_icon)
    ImageView weiXinIcon;
    @BindView(R.id.alipay_icon)
    ImageView alipayIcon;
    @BindView(R.id.balancePay_icon)
    ImageView balancePayIcon;
    @BindView(R.id.edit_amount)
    EditText editAmount;
    @BindView(R.id.imgView)
    RoundedImageView imgView;
    @BindView(R.id.tvName)
    TextView tvName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_payment;
    }

    @Override
    protected void initView() {
        titleName.setText("付款");
        EventBus.getDefault().register(this);
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        EditTextUtils.afterDotTwo(editAmount);
        payInfo = getIntent().getStringExtra(PAYMENT_INFO);
        weiXinIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
        String[] result = payInfo.split("=");
        merchantsId = result[1];
    }

    @Override
    protected void initData() {
        OkHttpUtils.get()
                .url(Urls.GET_MERCHANTS_DETAIL)
                .addParams(ACCESSTOKEN, token)
                .addParams(USERID, userId)
                .addParams("merchantId", merchantsId + "")
                .build()
                .execute(new Callback<MerchantsModel>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(MerchantsModel response, int id) {
                        if (response != null) {
                            merchantsBean = response;
                            tvName.setText("付款给" + merchantsBean.getMerchantName());
                            Glide.with(MyPaymentActivity.this).load(merchantsBean.getLogo()).apply(new RequestOptions().error(R.mipmap.g10_04weijiazai).placeholder(R.mipmap.g10_04weijiazai)).into(imgView);
                        }
                    }
                });

    }

    @OnClick({R.id.left_button, R.id.tv_pay, R.id.weixinPay_icon, R.id.alipay_icon, R.id.balancePay_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.tv_pay:
                if (TextUtils.isEmpty(editAmount.getText().toString())) {
                    ToastUtils.showShortToast("请输入正确金额");
                    return;
                }
                if (editAmount.getText().toString().endsWith(".")) {
                    ToastUtils.showShortToast("请输入正确金额");
                    return;
                }
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
        OkHttpUtils.post()
                .url(Urls.RECEIVABLES_PAY_SETMEAL)
                .addParams(ACCESSTOKEN, token)//
                .addParams(USERID, userId)//
                .addParams("appId", appId)  //这个是微信才需要的，
                .addParams("merchantId", merchantsId)
                .addParams("channel", payChannel)
                .addParams("amount", editAmount.getText().toString().trim())
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
        IWXAPI api = WXAPIFactory.createWXAPI(MyPaymentActivity.this, result.appid);
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

        PayUtils.aliPay(MyPaymentActivity.this, payProof, new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                ToastUtils.showShortToast("支付成功");
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
        if (msg.getCurrentInfo() == PayInfo.RECEIVABLES_SETMEAL_PAY) {
            if (msg.getIsSuccess() == PayInfo.SUCCESS) {
                ToastUtils.showShortToast("支付成功");
                finish();
            } else {
                ToastUtils.showShortToast("支付失败");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
