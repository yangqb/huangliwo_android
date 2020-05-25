package com.feitianzhu.huangliwo.shop;

import android.content.Context;

import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.common.impl.onNetFinishLinstenerT;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.model.ShopsEvali;
import com.feitianzhu.huangliwo.model.ShopsNearby;
import com.feitianzhu.huangliwo.model.UserAuth;
import com.feitianzhu.huangliwo.model.WXModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.lzy.okgo.OkGo;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.LOAD_USER_AUTH;
import static com.feitianzhu.huangliwo.common.Constant.USERID;

/**
 * Created by dicallc on 2017/9/4 0004.
 */

public class ShopDao {

    public static void loadShopsInfo(Context context, String merchantid,
                                     final onConnectionFinishLinstener mLinstener) {
        String token = SPUtils.getString(context, Constant.SP_ACCESS_TOKEN);
        String userId = SPUtils.getString(context, Constant.SP_LOGIN_USERID);
    }

    public static void LoadNearbyShops(int page, String provinceId, String cityId, String clsId,
                                       final onNetFinishLinstenerT<ShopsNearby> mLinstener) {
    }


    public static void loadShopsServiceEvalu(int page, String mMerchantId, final onNetFinishLinstenerT<ShopsEvali> mLinstener) {
    }

    /**
     * 获取用户授权信息
     */
    public static void loadUserAuthImpl(Context context) {
        Constant.loadUserAuth = false;
        Constant.mUserAuth = null;
        String token = SPUtils.getString(context, Constant.SP_ACCESS_TOKEN);
        String userId = SPUtils.getString(context, Constant.SP_LOGIN_USERID);
        OkGo.<LzyResponse<UserAuth>>post(Urls.BASE_URL + LOAD_USER_AUTH)
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)//
                .execute(new JsonCallback<LzyResponse<UserAuth>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse<UserAuth>> response) {
                        if (response.body().data != null && response.body().code == 0) {
                            Constant.mUserAuth = response.body().data;
                            Constant.loadUserAuth = true;
                        }else {
                            Constant.loadUserAuth = false;
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse<UserAuth>> response) {
                        super.onError(response);
                        Constant.loadUserAuth = false;
                    }
                });
    }

    public static void postShopReport(String id, String type, String reportContent,
                                      final onConnectionFinishLinstener mLinstener) {
    }

    /*
     * 微信充值支付
     * */
    public static void postWxPay(String money, String chanel, final onNetFinishLinstenerT<WXModel> mLinstener) {
    }

    /*
     * 阿里充值支付
     * */
    public static void postAliPayPay(String money, final onConnectionFinishLinstener mLinstener) {

    }

    public static void postShopEva(String orderNo, String content, String star, final onConnectionFinishLinstener mLinstener) {

    }
}
