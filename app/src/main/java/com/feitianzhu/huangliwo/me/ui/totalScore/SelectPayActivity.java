package com.feitianzhu.huangliwo.me.ui.totalScore;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.dao.NetworkDao;
import com.feitianzhu.huangliwo.home.WebViewActivity;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.me.helper.CityModel;
import com.feitianzhu.huangliwo.me.helper.DialogHelper;
import com.feitianzhu.huangliwo.model.PayInfo;
import com.feitianzhu.huangliwo.model.SelectPayNeedModel;
import com.feitianzhu.huangliwo.model.ShopRecordWxModel;
import com.feitianzhu.huangliwo.payforme.PayForMeEvent;
import com.feitianzhu.huangliwo.payforme.PayForMeRecordActivity;
import com.feitianzhu.huangliwo.shop.ShopDao;
import com.feitianzhu.huangliwo.shop.ShopHelp;
import com.feitianzhu.huangliwo.utils.PayUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.google.gson.Gson;
import com.socks.library.KLog;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Vya on 2017/9/5 0005.
 */

public class SelectPayActivity extends BaseActivity {
    @BindView(R.id.iv_check1)
    ImageView mIvCheck1;
    @BindView(R.id.iv_check2)
    ImageView mIvCheck2;
    @BindView(R.id.iv_check3)
    ImageView mIvCheck3;
    @BindView(R.id.iv_check4)
    ImageView mIvCheck4;
    @BindView(R.id.tv_payMoney)
    TextView mPayMoney;
    @BindView(R.id.ll_protocol)
    LinearLayout ll_protocol;
    @BindView(R.id.cb_protocol)
    CheckBox cb_protocol;
    @BindView(R.id.tv_protocol)
    TextView mTvProtocol;
    @BindView(R.id.payWhat)
    TextView mPayWhatName;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;


    @BindView(R.id.rl_selectcity_container)
    RelativeLayout mSelectCityContainer;// 选择地区
    @BindView(R.id.selectCity)
    TextView mSelectCity;

    private String provinceId = "";
    private String provinceName = "";
    private String cityId = "";
    private String cityName = "";
    private String areaId = "";
    private String areaName = "";

    private List<ImageView> mClick;
    private SelectPayNeedModel mSelectModel;
    private CityModel mCityModel;
    private String xieyiUrl = "";

    private String mProtocolName = "";

    @Override
    protected void onStart() {
        super.onStart();
        ShopDao.loadUserAuthImpl(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_pay;
    }

    @Override
    protected void initTitle() {
        titleName.setText("选择支付");
        rightText.setText("取消");
        rightText.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initView() {
        //通过SelectPayNeedModel封装参数传递过来
        mClick = new ArrayList<>();
        Intent intent = getIntent();
        mSelectModel = (SelectPayNeedModel) intent.getSerializableExtra(Constant.INTENT_SELECTET_PAY_MODEL);
        //  Log.e("wangyan","---->"+ mSelectModel.toString());
        if (mSelectModel.getHandleFee() >= 0) {
            mPayMoney.setText(mSelectModel.getHandleFee() + "");
        }
        if ("0".equals(mSelectModel.agentType)) { //志愿者协议
            xieyiUrl = "http://192.168.0.06:8089/static/volunteer_protocol.html";
            mProtocolName = "志愿者注册协议";
        } else {  //合伙人协议
            xieyiUrl = "http://192.168.0.06:8089/static/agent_protocol.html";
            mProtocolName = "合伙人注册协议";

        }
        Log.e("Test", "agentName====" + mSelectModel.agentName + "-----agentType---" + mSelectModel.agentType);
        if (!TextUtils.isEmpty(mSelectModel.agentName)) {
            mPayWhatName.setText("成为" + mSelectModel.agentName + ":");
        }

        mTvProtocol.setText("《" + mProtocolName + "》");
        if (mSelectModel.getType() == SelectPayNeedModel.TYPE_UNION_LEVEL) {
            ll_protocol.setVisibility(View.VISIBLE);
        }


        if ("4".equals(mSelectModel.agentType)) { //区代
            mSelectCityContainer.setVisibility(View.VISIBLE);
            mSelectCityContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //显示三个的
                    new DialogHelper(SelectPayActivity.this).init(DialogHelper.DIALOG_CITY_THREE, v).buildDialog(new DialogHelper.OnButtonClickListener<CityModel>() {

                        @Override
                        public void onButtonClick(View v, CityModel result, View clckView) {
                            mCityModel = result;
                            mCityModel.agentType = "4";
                            mSelectCity.setText(result.getCity() + "  " + result.getArea() + " " + result.getLocal());
                        }
                    });
                }
            });
        }
        if ("3".equals(mSelectModel.agentType)) {
            mSelectCityContainer.setVisibility(View.VISIBLE);
            mSelectCityContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //显示选择城市的dialog显示2个   mSelectCity设置显示
                    new DialogHelper(SelectPayActivity.this).init(DialogHelper.DIALOG_TWO, v).buildDialog(new DialogHelper.OnButtonClickListener<CityModel>() {

                        @Override
                        public void onButtonClick(View v, CityModel result, View clickView) {
                            mCityModel = result;
                            mCityModel.agentType = "3";
                            mSelectCity.setText(result.getCity() + "  " + result.getArea());
                        }
                    });
                }
            });
        }

        if ("2".equals(mSelectModel.agentType)) {
            mSelectCityContainer.setVisibility(View.VISIBLE);
            mSelectCityContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //显示选择城市的dialog显示1个   mSelectCity设置显示
                    new DialogHelper(SelectPayActivity.this).init(DialogHelper.DIALOG_ONE_CITY, v).buildDialog(new DialogHelper.OnButtonClickListener<CityModel>() {

                        @Override
                        public void onButtonClick(View v, CityModel result, View clickView) {
                            mCityModel = result;
                            mCityModel.agentType = "2";
                            mSelectCity.setText(result.getCity());
                        }
                    });
                }
            });
        }
    }


    @Override
    protected void initData() {
        setShowData();
        EventBus.getDefault().register(this);
    }

    private void setShowData() {
        initCheckPic();
        mIvCheck1.setImageResource(R.drawable.icon_xuanze);
        mClick.add(mIvCheck1);
    }

    @OnClick({R.id.iv_check1, R.id.iv_check2, R.id.iv_check3, R.id.iv_check4, R.id.bt_ImmediatePay, R.id.tv_protocol, R.id.left_button, R.id.right_button})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_button:
            case R.id.right_button:
                finish();
                break;
            case R.id.iv_check1:
                if (!mClick.contains(mIvCheck1)) {
                    mClick.remove(0).setImageResource(R.drawable.icon_weixuanze);
                    mIvCheck1.setImageResource(R.drawable.icon_xuanze);
                    mClick.clear();
                    mClick.add(mIvCheck1);

                }
                break;
            case R.id.iv_check2:

                if (!mClick.contains(mIvCheck2)) {
                    mClick.remove(0).setImageResource(R.drawable.icon_weixuanze);
                    mIvCheck2.setImageResource(R.drawable.icon_xuanze);
                    mClick.clear();
                    mClick.add(mIvCheck2);

                }
                break;
            case R.id.iv_check3:
                if (!mClick.contains(mIvCheck3)) {
                    mClick.remove(0).setImageResource(R.drawable.icon_weixuanze);
                    mIvCheck3.setImageResource(R.drawable.icon_xuanze);
                    mClick.clear();
                    mClick.add(mIvCheck3);

                }
                break;
            case R.id.iv_check4:
                if (!mClick.contains(mIvCheck4)) {
                    mClick.remove(0).setImageResource(R.drawable.icon_weixuanze);
                    mIvCheck4.setImageResource(R.drawable.icon_xuanze);
                    mClick.clear();
                    mClick.add(mIvCheck4);

                }
                break;
            case R.id.cb_protocol:
                if (cb_protocol.isChecked()) {
                    cb_protocol.setChecked(false);
                } else {
                    cb_protocol.setChecked(true);
                }
                break;

            case R.id.tv_protocol:
                WebViewActivity.startActivity(this, xieyiUrl, mProtocolName);
                break;

            case R.id.bt_ImmediatePay:
                String str = "";
                String text = (String) mClick.get(0).getTag();
                mSelectModel.setPayChannel(text);
                if (text.equals("wx")) {
                    str = "微信支付";
                    sendParamsData();
                } else if (text.equals("alipay")) {
                    str = "支付宝支付";
                    sendParamsData();
                } else if (text.equals("balance")) {
                    str = "余额支付";
                    sendParamsData();
                } else if (text.equals("offline")) {
                    str = "线下转账";
                    Intent intent = new Intent(SelectPayActivity.this, TransferVoucherActivity.class);
                    intent.putExtra("transferNeedModel", mSelectModel);
                    startActivity(intent);
                }

                break;
        }
    }

    private void sendParamsData() {

        if (mSelectModel == null) {
            return;
        }
        if (ll_protocol.getVisibility() == View.VISIBLE && !cb_protocol.isChecked()) {
            ToastUtils.showLongToast("请勾选协议声明!");
            return;
        }

        ShopHelp.veriPassword(this, new onConnectionFinishLinstener() {
            @Override
            public void onSuccess(int code, Object result) {

                showloadDialog("支付中...");
                final String mPayChanel = mSelectModel.getPayChannel();
                if (mSelectModel.getType() == SelectPayNeedModel.TYPE_SHOP_RECORD) {
                    if (TextUtils.isEmpty(mSelectModel.getPlaceImgFile()) || TextUtils.isEmpty(mSelectModel.getObjImgFile()) || TextUtils.isEmpty(mSelectModel.getRcptImgFile())) {
                        ToastUtils.showLongToast("您未上传图片信息，请上传后重试");
                        return;
                    }
                    NetworkDao.PayShopRecord(SelectPayActivity.this, mSelectModel.getMemberId(), mSelectModel.getConsumeAmount() + "", mSelectModel.getHandleFee() + ""
                            , mSelectModel.getFeeId(), result.toString(), mPayChanel, mSelectModel.getPlaceImgFile(), mSelectModel.getObjImgFile(),
                            mSelectModel.getRcptImgFile(), new onConnectionFinishLinstener() {
                                @Override
                                public void onSuccess(int code, Object result) {
                                    goneloadDialog();
                                    if (mPayChanel.equals("balance")) {
                                                                               ToastUtils.showLongToast("支付成功");
                                        finish();
                                    }

                                    if (mPayChanel.equals("wx")) {
                                        if (result == null) {
                                            ToastUtils.showShortToast("微信支付失败");
                                            return;
                                        }
                                        Constant.PayFlag = PayInfo.SHOPRECORDER;
                                        CallWxPay(result.toString());

                                    }

                                    if (mPayChanel.equals("alipay")) {
                                        final String orderInfo = result.toString();
                                        PayUtils.aliPay(SelectPayActivity.this, orderInfo, new onConnectionFinishLinstener() {
                                            @Override
                                            public void onSuccess(int code, Object result) {
                                                ToastUtils.showShortToast("支付成功");
                                                finish();

                                            }

                                            @Override
                                            public void onFail(int code, String result) {
                                                ToastUtils.showShortToast("支付失败");
                                                EventBus.getDefault().post(PayForMeEvent.PAY_FAILURE);
                                                goneloadDialog();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFail(int code, String result) {
                                    ToastUtils.showLongToast(result);
                                    goneloadDialog();
                                }
                            });

                } else if (mSelectModel.getType() == SelectPayNeedModel.TYPE_PAY_FOR_ME) {

                    NetworkDao.payForMe(SelectPayActivity.this, result.toString(), mSelectModel.getMerchantName(), mSelectModel.getMerchantAddr(), mSelectModel.getGoodsName()
                            , mSelectModel.getConsumeAmount() + "", mSelectModel.getHandleFee() + "", mSelectModel.getPayChannel(),
                            mSelectModel.getPlaceImgFile(), mSelectModel.getObjImgFile(), mSelectModel.getRcptImgFile(), "", new onConnectionFinishLinstener() {
                                @Override
                                public void onSuccess(int code, Object result) {
                                    goneloadDialog();
                                    if ("alipay".equals(mSelectModel.getPayChannel())) {

                                        final String orderInfo = result.toString();
                                        PayUtils.aliPay(SelectPayActivity.this, orderInfo, new onConnectionFinishLinstener() {
                                            @Override
                                            public void onSuccess(int code, Object result) {
                                                ToastUtils.showShortToast("支付成功");
                                                EventBus.getDefault().post(PayForMeEvent.PAY_SUCCESS);
                                                finish();
                                                startActivity(new Intent(SelectPayActivity.this, PayForMeRecordActivity.class));
                                            }

                                            @Override
                                            public void onFail(int code, String result) {
                                                ToastUtils.showShortToast("支付失败");
                                                EventBus.getDefault().post(PayForMeEvent.PAY_FAILURE);
                                                goneloadDialog();
                                            }
                                        });
                                    } else if ("wx".equals(mSelectModel.getPayChannel())) {
                                        if (mPayChanel.equals("wx")) {
                                            if (result == null) {
                                                ToastUtils.showShortToast("微信支付失败");
                                                return;
                                            }
                                            Constant.PayFlag = PayInfo.PAY_FORME;
                                            CallWxPay(result.toString());

                                        }
                                    } else {
                                        ToastUtils.showShortToast("支付成功");
                                        KLog.e(result);
                                        EventBus.getDefault().post(PayForMeEvent.PAY_SUCCESS);
                                        finish();
                                        startActivity(new Intent(SelectPayActivity.this, PayForMeRecordActivity.class));
                                    }
                                }

                                @Override
                                public void onFail(int code, String result) {
                                    goneloadDialog();
                                    ToastUtils.showShortToast(result);
                                    EventBus.getDefault().post(PayForMeEvent.PAY_FAILURE);
                                }
                            });
                } else if (mSelectModel.getType() == SelectPayNeedModel.TYPE_UNION_LEVEL) {
                    String gradeid = "";
                    if (mSelectModel != null && mSelectModel.gradeId != null) {
                        gradeid = mSelectModel.gradeId;
                    } else {
                        gradeid = "";
                    }
                    if ("2".equals(mSelectModel.agentType) && TextUtils.isEmpty(mCityModel.cityId)) {
                        ToastUtils.showShortToast("必须要选择省份");
                        return;
                    }
                    NetworkDao.PayUnionLevel(SelectPayActivity.this, gradeid, result.toString(), mSelectModel.getPayChannel(), mCityModel, new onConnectionFinishLinstener() {
                        @Override
                        public void onSuccess(int code, Object result) {
                            goneloadDialog();
                            if ("alipay".equals(mSelectModel.getPayChannel())) {

                                final String orderInfo = result.toString();
                                PayUtils.aliPay(SelectPayActivity.this, orderInfo, new onConnectionFinishLinstener() {
                                    @Override
                                    public void onSuccess(int code, Object result) {
                                        ToastUtils.showShortToast("支付成功");
                                        EventBus.getDefault().post(PayForMeEvent.PAY_SUCCESS);
                                        finish();
                                                                         }

                                    @Override
                                    public void onFail(int code, String result) {
                                        ToastUtils.showShortToast("支付失败");
                                        EventBus.getDefault().post(PayForMeEvent.PAY_FAILURE);
                                    }
                                });
                            } else if ("wx".equals(mSelectModel.getPayChannel())) {
                                if (result == null) {
                                    ToastUtils.showShortToast("微信支付失败");
                                    return;
                                }
                                Constant.PayFlag = PayInfo.UNIONLEVEL;
                                CallWxPay(result.toString());
                            } else {
                                ToastUtils.showShortToast("支付成功");
                                KLog.e(result);
                                EventBus.getDefault().post(PayForMeEvent.PAY_SUCCESS);
                                finish();
                                                           }
                        }

                        @Override
                        public void onFail(int code, String result) {
                            goneloadDialog();
                            ToastUtils.showShortToast(result);
                            EventBus.getDefault().post(PayForMeEvent.PAY_FAILURE);
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPayMessageCall(PayInfo msg) {
        switch (msg.getCurrentInfo()) {
            case PayInfo.SHOPRECORDER:
                Log.e("Test", "msg.getCurrentInfo()---------" + msg.getCurrentInfo());
                goneloadDialog();

                finish();
                break;
            case PayInfo.UNIONLEVEL:
                goneloadDialog();
                finish();
                break;
            case PayInfo.PAY_FORME:
                goneloadDialog();
                finish();
                startActivity(new Intent(SelectPayActivity.this, PayForMeRecordActivity.class));
                EventBus.getDefault().post(PayForMeEvent.PAY_SUCCESS);
                break;
        }
    }

    private void CallWxPay(String result) {

        Log.e("Test", "--------result----->" + result);
        Gson gson = new Gson();
        ShopRecordWxModel mResult = gson.fromJson(result.toString(), ShopRecordWxModel.class);
        IWXAPI api = WXAPIFactory.createWXAPI(SelectPayActivity.this, mResult.getAppid());
        api.registerApp(mResult.getAppid());
        PayReq mPayReq = new PayReq();
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

    public void initCheckPic() {
        mIvCheck1.setBackgroundResource(R.drawable.icon_weixuanze);
        mIvCheck2.setBackgroundResource(R.drawable.icon_weixuanze);
        mIvCheck3.setBackgroundResource(R.drawable.icon_weixuanze);
        mIvCheck4.setBackgroundResource(R.drawable.icon_weixuanze);
    }

}
