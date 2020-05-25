package com.feitianzhu.huangliwo.wxapi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.model.PayInfo;
import com.hjq.toast.ToastUtils;
import com.socks.library.KLog;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by jiangdikai on 2017/9/26.
 */

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID);
        api.registerApp(Constant.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onReq(BaseReq baseReq) {
        KLog.i("WXPayEntryActivity onReq..." + baseReq.toString());
    }

    @Override
    public void onResp(BaseResp resp) {
        KLog.i("WXPayEntryActivity onResp..." + resp.toString());
        Log.e("Test", "-------WXPayEntryActivity  onResp..." + resp.toString());
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                switch (Constant.PayFlag) {
                    case PayInfo.MyMoneyPay:
                        EventBus.getDefault().post(new PayInfo(PayInfo.MyMoneyPay, PayInfo.SUCCESS));
                        break;
                    case PayInfo.ShopPay:
                        EventBus.getDefault().post(new PayInfo(PayInfo.ShopPay, PayInfo.SUCCESS));
                        break;
                    case PayInfo.SHOPRECORDER:
                        EventBus.getDefault().post(new PayInfo(PayInfo.SHOPRECORDER, PayInfo.SUCCESS));
                        break;
                    case PayInfo.UNIONLEVEL:
                        EventBus.getDefault().post(new PayInfo(PayInfo.UNIONLEVEL, PayInfo.SUCCESS));
                        break;
                    case PayInfo.PAY_FORME:
                        EventBus.getDefault().post(new PayInfo(PayInfo.PAY_FORME, PayInfo.SUCCESS));
                        break;
                    case PayInfo.BUY_SERVICE:
                        EventBus.getDefault().post(new PayInfo(PayInfo.BUY_SERVICE, PayInfo.SUCCESS));
                        break;
                    case PayInfo.SETMEAL_PAY:
                        EventBus.getDefault().post(new PayInfo(PayInfo.SETMEAL_PAY, PayInfo.SUCCESS));
                        break;
                    case PayInfo.RECEIVABLES_SETMEAL_PAY:
                        EventBus.getDefault().post(new PayInfo(PayInfo.RECEIVABLES_SETMEAL_PAY, PayInfo.SUCCESS));
                        break;
                    case PayInfo.SHOPPING_CART_PAY:
                        EventBus.getDefault().post(new PayInfo(PayInfo.SHOPPING_CART_PAY, PayInfo.SUCCESS));
                        break;
                }
            } else {
                switch (Constant.PayFlag) {
                    case PayInfo.MyMoneyPay:
                        EventBus.getDefault().post(new PayInfo(PayInfo.MyMoneyPay, PayInfo.FAIL));
                        break;
                    case PayInfo.ShopPay:
                        EventBus.getDefault().post(new PayInfo(PayInfo.ShopPay, PayInfo.FAIL));
                        break;
                    case PayInfo.SHOPRECORDER:
                        EventBus.getDefault().post(new PayInfo(PayInfo.SHOPRECORDER, PayInfo.FAIL));
                        break;
                    case PayInfo.UNIONLEVEL:
                        EventBus.getDefault().post(new PayInfo(PayInfo.UNIONLEVEL, PayInfo.FAIL));
                        break;
                    case PayInfo.PAY_FORME:
                        EventBus.getDefault().post(new PayInfo(PayInfo.PAY_FORME, PayInfo.FAIL));
                        break;
                    case PayInfo.BUY_SERVICE:
                        EventBus.getDefault().post(new PayInfo(PayInfo.BUY_SERVICE, PayInfo.FAIL));
                        break;
                    case PayInfo.SETMEAL_PAY:
                        EventBus.getDefault().post(new PayInfo(PayInfo.SETMEAL_PAY, PayInfo.FAIL));
                        break;
                    case PayInfo.RECEIVABLES_SETMEAL_PAY:
                        EventBus.getDefault().post(new PayInfo(PayInfo.RECEIVABLES_SETMEAL_PAY, PayInfo.FAIL));
                        break;
                    case PayInfo.SHOPPING_CART_PAY:
                        EventBus.getDefault().post(new PayInfo(PayInfo.SHOPPING_CART_PAY, PayInfo.FAIL));
                        break;
                }

            }
        } else {
            ToastUtils.show(resp.getType() + "");
        }
        finish();
    }
}
