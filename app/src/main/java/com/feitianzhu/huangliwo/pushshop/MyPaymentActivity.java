package com.feitianzhu.huangliwo.pushshop;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.pushshop.bean.PaymentInfo;
import com.feitianzhu.huangliwo.utils.EditTextUtils;
import com.feitianzhu.huangliwo.utils.PayUtils;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.hjq.toast.ToastUtils;
import com.itheima.roundedimageview.RoundedImageView;
import com.lzy.okgo.OkGo;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

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
        OkGo.<LzyResponse<MerchantsModel>>get(Urls.GET_MERCHANTS_DETAIL)
                .tag(this)
                .params(ACCESSTOKEN, token)
                .params(USERID, userId)
                .params("merchantId", merchantsId + "")
                .execute(new JsonCallback<LzyResponse<MerchantsModel>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<MerchantsModel>> response) {
                        super.onSuccess(MyPaymentActivity.this, response.body().msg, response.body().code);
                        if (response.body().code == 0 && response.body().data != null) {
                            merchantsBean = response.body().data;
                            tvName.setText("付款给" + merchantsBean.getMerchantName());
                            Glide.with(MyPaymentActivity.this).load(merchantsBean.getLogo()).apply(new RequestOptions().error(R.mipmap.g10_04weijiazai).placeholder(R.mipmap.g10_04weijiazai)).into(imgView);
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<MerchantsModel>> response) {
                        super.onError(response);
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
                    ToastUtils.show("请输入正确金额");
                    return;
                }
                if (editAmount.getText().toString().endsWith(".")) {
                    ToastUtils.show("请输入正确金额");
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

        OkGo.<LzyResponse<PayModel>>post(Urls.RECEIVABLES_PAY_SETMEAL)
                .tag(this)
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)//
                .params("appId", appId)  //这个是微信才需要的，
                .params("merchantId", merchantsId)
                .params("channel", payChannel)
                .params("amount", editAmount.getText().toString().trim())
                .execute(new JsonCallback<LzyResponse<PayModel>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<PayModel>> response) {
                        super.onSuccess(MyPaymentActivity.this, response.body().msg, response.body().code);
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
        Constant.PayFlag = PayInfo.RECEIVABLES_SETMEAL_PAY;
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
        ToastUtils.show("正在打开微信中");
    }


    private void aliPay(String payProof, String orderNo) {

        PayUtils.aliPay(MyPaymentActivity.this, payProof, new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                ToastUtils.show("支付成功");
                finish();
            }

            @Override
            public void onFail(int code, String result) {
                ToastUtils.show("支付失败");
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPayMessageCall(PayInfo msg) {
        if (msg.getCurrentInfo() == PayInfo.RECEIVABLES_SETMEAL_PAY) {
            if (msg.getIsSuccess() == PayInfo.SUCCESS) {
                ToastUtils.show("支付成功");
                finish();
            } else {
                ToastUtils.show("支付失败");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
