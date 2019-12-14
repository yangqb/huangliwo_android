package com.feitianzhu.fu700.vip;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.common.Constant;
import com.feitianzhu.fu700.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.fu700.dao.NetworkDao;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.me.helper.CityModel;
import com.feitianzhu.fu700.me.ui.ShopRecordDetailActivity;
import com.feitianzhu.fu700.me.ui.UnionApplyRecordActivity;
import com.feitianzhu.fu700.model.PayInfo;
import com.feitianzhu.fu700.model.SelectPayNeedModel;
import com.feitianzhu.fu700.model.ShopRecordWxModel;
import com.feitianzhu.fu700.payforme.PayForMeEvent;
import com.feitianzhu.fu700.payforme.PayForMeRecordActivity;
import com.feitianzhu.fu700.shop.ShopDao;
import com.feitianzhu.fu700.shop.ShopHelpTwo;
import com.feitianzhu.fu700.utils.PayUtils;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.utils.Urls;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
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

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @class name：com.feitianzhu.fu700.vip
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/23 0023 上午 11:44
 */
public class VipUpgradeActivity extends BaseActivity {

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
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    private SelectPayNeedModel mSelectModel;
    private String payType = "wx"; //支付方式
    private CityModel mCityModel;
    private String orderNo = "";//订单号

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip_upgrade;
    }

    @Override
    protected void onStart() {
        super.onStart();
        ShopDao.loadUserAuthImpl();
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        mSelectModel = (SelectPayNeedModel) intent.getSerializableExtra(Constant.INTENT_SELECTET_PAY_MODEL);
        mSelectModel.setPayChannel("wx"); //默认微信支付
        titleName.setText("确认升级");
        weiXinIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
        rlAddress.setVisibility(View.GONE); //暂时不提供地址选择
        /**
         * Spanned.SPAN_INCLUSIVE_EXCLUSIVE 从起始下标到终了下标，包括起始下标
         * Spanned.SPAN_INCLUSIVE_INCLUSIVE 从起始下标到终了下标，同时包括起始下标和终了下标
         * Spanned.SPAN_EXCLUSIVE_EXCLUSIVE 从起始下标到终了下标，但都不包括起始下标和终了下标
         * Spanned.SPAN_EXCLUSIVE_INCLUSIVE 从起始下标到终了下标，包括终了下标
         */
        bottomAmount.setText("");
        topAmount.setText("");
        String str1 = "合计：";
        String str2 = "¥ ";
        String str3 = "399.00";
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

        SpannableString span4 = new SpannableString(str2);
        SpannableString span5 = new SpannableString(str3);
        ForegroundColorSpan colorSpan4 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span4.setSpan(new AbsoluteSizeSpan(11, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span4.setSpan(colorSpan4, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan5 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span5.setSpan(new AbsoluteSizeSpan(18, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span5.setSpan(colorSpan5, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        topAmount.append(span4);
        topAmount.append(span5);

        //showDialog();

    }

    @OnClick({R.id.left_button, R.id.weixinPay_icon, R.id.alipay_icon, R.id.balancePay_icon, R.id.tv_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.weixinPay_icon:
                weiXinIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
                alipayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                balancePayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                mSelectModel.setPayChannel("wx");
                break;
            case R.id.alipay_icon:
                weiXinIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                alipayIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
                balancePayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                mSelectModel.setPayChannel("alipay");
                break;
            case R.id.balancePay_icon:
                weiXinIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                alipayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                balancePayIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
                mSelectModel.setPayChannel("");
                break;
            case R.id.tv_pay:
                pay();
                break;
        }
    }

    public void pay() {

        if (mSelectModel == null) {
            return;
        }
        ShopHelpTwo.veriPassword(this, new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                showloadDialog("支付中...");
                if (mSelectModel.getType() == SelectPayNeedModel.TYPE_UNION_LEVEL) {
                    String gradeid = "";
                    if (mSelectModel != null && mSelectModel.gradeId != null) {
                        gradeid = mSelectModel.gradeId;
                    } else {
                        gradeid = "";
                    }
                    //这里的判断不知道怎么弄得，没有选择市区(先暂时不管这个段代码)
                    /*if ("2".equals(mSelectModel.agentType) && TextUtils.isEmpty(mCityModel.cityId)) {
                        ToastUtils.showShortToast("必须要选择省份");
                        return;
                    }*/
                    NetworkDao.PayUnionLevel(gradeid, result.toString(), mSelectModel.getPayChannel(), mCityModel, new onConnectionFinishLinstener() {
                        @Override
                        public void onSuccess(int code, Object result) {
                            goneloadDialog();
                            if ("alipay".equals(mSelectModel.getPayChannel())) {
                                JSONObject object = null;
                                try {
                                    object = new JSONObject(result.toString());
                                    String orderInfo = object.getString("payParam");
                                    orderNo = object.getString("orderNo");
                                    aliPay(orderInfo, orderNo);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else if ("wx".equals(mSelectModel.getPayChannel())) {
                                Constant.PayFlag = PayInfo.UNIONLEVEL;
                                CallWxPay(result.toString());
                            } else {
                                /*
                                 * 这里不知道什么鬼逻辑 直接就是支付成功（保证代码不走进来就OK）
                                 * */
                                ToastUtils.showShortToast("支付成功");
                                KLog.e(result);
                                EventBus.getDefault().post(PayForMeEvent.PAY_SUCCESS);
                                finish();
                                startActivity(new Intent(VipUpgradeActivity.this, UnionApplyRecordActivity.class));
                            }
                        }

                        @Override
                        public void onFail(int code, String result) {
                            goneloadDialog();
                            ToastUtils.showShortToast(result);
                            EventBus.getDefault().post(PayForMeEvent.PAY_FAILURE);
                            ToastUtils.showShortToast("提交订单失败");
                        }
                    });
                }
            }

            @Override
            public void onFail(int code, String result) {
                goneloadDialog();
                ToastUtils.showShortToast(result);
            }
        });

    }

    public void aliPay(String orderInfo, String orderNO) {
        PayUtils.aliPay(VipUpgradeActivity.this, orderInfo, new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {
                ToastUtils.showShortToast("支付成功");
                mSelectModel.setIsPay("1");
                //弹框
                showDialog();
            }

            @Override
            public void onFail(int code, String result) {
                mSelectModel.setIsPay("0");
                ToastUtils.showShortToast("支付失败");
                EventBus.getDefault().post(PayForMeEvent.PAY_FAILURE);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPayMessageCall(PayInfo msg) {
        switch (msg.getCurrentInfo()) {
            case PayInfo.SHOPRECORDER:
                Log.e("Test", "msg.getCurrentInfo()---------" + msg.getCurrentInfo());
                goneloadDialog();
                startActivity(new Intent(VipUpgradeActivity.this, ShopRecordDetailActivity.class));
                finish();
                break;
            case PayInfo.UNIONLEVEL:
                goneloadDialog();
                if (msg.getIsSuccess() == PayInfo.SUCCESS) {
                    mSelectModel.setIsPay("1");
                    showDialog();
                } else {
                    mSelectModel.setIsPay("0");
                    EventBus.getDefault().post(PayForMeEvent.PAY_FAILURE);
                }
                break;
            case PayInfo.PAY_FORME:
                goneloadDialog();
                finish();
                startActivity(new Intent(VipUpgradeActivity.this, PayForMeRecordActivity.class));
                EventBus.getDefault().post(PayForMeEvent.PAY_SUCCESS);
                break;
        }
    }

    public void showDialog() {
        new XPopup.Builder(this)
                .enableDrag(false)
                .asCustom(new CustomPopup(this).onClose(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(PayForMeEvent.PAY_FINISH);
                        finish();
                    }
                }))
                .show();
    }

    private void CallWxPay(String result) {

        Log.e("Test", "--------result----->" + result);
        Gson gson = new Gson();
        ShopRecordWxModel mResult = gson.fromJson(result.toString(), ShopRecordWxModel.class);
        orderNo = mResult.getOrderNo();
        IWXAPI api = WXAPIFactory.createWXAPI(VipUpgradeActivity.this, mResult.getAppid());
        api.registerApp(mResult.getAppid());
        PayReq mPayReq = new PayReq();
        Constant.WX_APP_ID = mResult.getAppid() + "";
        mPayReq.appId = mResult.getAppid();
        mPayReq.partnerId = mResult.getPartnerid();
        mPayReq.prepayId = mResult.getPrepayid();
        mPayReq.packageValue = "Sign=WXPay";
        mPayReq.nonceStr = mResult.getNoncestr();
        mPayReq.timeStamp = mResult.getTimestamp() + "";
        mPayReq.sign = mResult.getSign();
        api.sendReq(mPayReq);
        ToastUtils.showShortToast("正在打开微信中");
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
