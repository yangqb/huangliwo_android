package com.feitianzhu.huangliwo.utils;

import android.app.Activity;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.model.PayResult;
import com.socks.library.KLog;

import java.util.Map;

import static com.feitianzhu.huangliwo.common.Constant.FailCode;
import static com.feitianzhu.huangliwo.common.Constant.SuccessCode;

/**
 * Created by dicallc on 2017/10/12 0012.
 */

public class PayUtils {

    /**
     * @param mActivity  调用所在Activity
     * @param orderInfo  后台返回字段payParamd的value
     * @param mLinstener 接口回调
     */

    public static void aliPay(final Activity mActivity, String orderInfo,
                              final onConnectionFinishLinstener mLinstener) {
        KLog.i("aliPay orderInfo: " + orderInfo);

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(mActivity);
                final Map<String, String> result = alipay.payV2(orderInfo, true);
                KLog.i("map" + result.toString());
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        @SuppressWarnings("unchecked")
                        PayResult payResult = new PayResult(result);

                        //对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = payResult.getResultStatus();
                        if (TextUtils.equals(resultStatus, "9000")) {
                            mLinstener.onSuccess(SuccessCode, resultInfo);
                        } else {
                            mLinstener.onFail(FailCode, payResult.getMemo());
                        }
                    }
                });
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
