package com.feitianzhu.huangliwo.dao;

import android.content.Context;
import android.text.TextUtils;

import com.feitianzhu.huangliwo.bankcard.entity.BankCardEntity;
import com.feitianzhu.huangliwo.bankcard.entity.UserBankCardEntity;
import com.feitianzhu.huangliwo.bankcard.entity.WithdrawFeeRateEntity;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.entity.DefaultRate;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.home.entity.NoticeEntity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.login.entity.LoginEntity;
import com.feitianzhu.huangliwo.login.entity.UserInfoEntity;
import com.feitianzhu.huangliwo.me.helper.CityModel;
import com.feitianzhu.huangliwo.model.OfflineModel;
import com.feitianzhu.huangliwo.payforme.entity.PayForMeEntity;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.socks.library.KLog;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by lijia on 2017/9/4 0004.
 */

public class NetworkDao {


    private NetworkDao() {
    }

    /* *//**
     * 获取验证码
     *//*
    public static void getSmsCode(Context context, String phone, String type, final onConnectionFinishLinstener linstener) {
        String token = SPUtils.getString(context, Constant.SP_ACCESS_TOKEN, "");
        String userId = SPUtils.getString(context, Constant.SP_LOGIN_USERID, "");
        OkHttpUtils
                .get()
                .url(Urls.GET_SMSCODE)
                .addParams("phone", phone)
                .addParams("type", type)
                .addParams("accessToken", token)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        KLog.i("mData: %s", mData);
                        return mData;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        linstener.onFail(FailCode, e.getMessage());
                        KLog.e(e);
                    }

                    @Override
                    public void onResponse(Object response, int id) {

                        KLog.i("response:%s", response.toString());

                        linstener.onSuccess(0, response);
                    }
                });
    }*/

    /**
     * 二级密码校验
     */
    public static void checkPayPwd(Context context, String paypass, final onConnectionFinishLinstener linstener) {
        String token = SPUtils.getString(context, Constant.SP_ACCESS_TOKEN);
        String userId = SPUtils.getString(context, Constant.SP_LOGIN_USERID);

        OkGo.<LzyResponse>post(Urls.CHECK_PAYPASS)
                .params(Constant.ACCESSTOKEN, token)
                .params(Constant.USERID, userId)
                .params("paypass", paypass)
                .execute(new JsonCallback<LzyResponse>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onSuccess(context, response.body().msg, response.body().code);
                        if (response.body().code == 0) {
                            linstener.onSuccess(response.body().code, response);
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<LzyResponse> response) {
                        super.onError(response);
                        linstener.onFail(response.body().code, response.message());
                    }
                });
    }

    /**
     * 新增银行卡信息
     *
     * @param bankId
     * @param openSubbranch 开户支行
     * @param bankCardNo    银行卡账号
     * @param paypass
     * @param linstener
     */
    public static void addBankCard(String realName, String bankId, String openSubbranch, String bankCardNo, String paypass, final onConnectionFinishLinstener linstener) {

    }

    /**
     * 获取用户银行卡列表
     */
    public static void getUserBankCardList(final onConnectionFinishLinstener linstener) {

    }

    /**
     * 获取银行卡列表
     */
    public static void getBankCardList(final onConnectionFinishLinstener linstener) {

    }

    public static void PayUnionLevel(Context context, String gradeId, String psw, final String PayChannel, CityModel model, final onConnectionFinishLinstener linstener) {

    }


    /**
     * 为我买单
     *
     * @param payPass       支付密码
     * @param merchantName  商户名称
     * @param merchantAddr  商户地址
     * @param goodsName     商品名称
     * @param consumeAmount 消费金额
     * @param handleFee     手续费（系统参数--策划推广比例接口返回的比例*消费金额）
     * @param payChannel    支付渠道（wx：微信，alipay：支付宝，balance：余额，offline：线下转账）
     * @param placeImgFile  消费场所图
     * @param objImgFile    消费实物图
     * @param rcptImgFile   消费发票图
     * @param payProofFile  转账凭证（线下支付时必传）
     * @param linstener
     */
    public static void payForMe(Context context, String payPass, String merchantName, String merchantAddr, String goodsName, String consumeAmount, String handleFee, final String payChannel,
                                String placeImgFile, String objImgFile, String rcptImgFile, String payProofFile, final onConnectionFinishLinstener linstener) {
    }

    /**
     * 为我买单记录
     *
     * @param status    审核状态（0：审核中，1：审核通过，-1：拒绝）
     * @param pageIndex 页数
     * @param pageRows  每页行数
     * @param linstener
     */
    public static void payForMeRecord(Context context, String status, String pageIndex, String pageRows, final onConnectionFinishLinstener linstener) {
        String token = SPUtils.getString(context, Constant.SP_ACCESS_TOKEN);
        String userId = SPUtils.getString(context, Constant.SP_LOGIN_USERID);

    }

    /**
     * 提现
     *
     * @param payPass
     * @param amount
     * @param bankCardId 银行卡id
     * @param type       类型（1：余额提现，2：商户钱包提现）
     * @param linstener
     */
    public static void withdraw(Context context, String payPass, String amount, String bankCardId, String type, final onConnectionFinishLinstener linstener) {
        String token = SPUtils.getString(context, Constant.SP_ACCESS_TOKEN);
        String userId = SPUtils.getString(context, Constant.SP_LOGIN_USERID);
    }


    /**
     * 提现手续费
     *
     * @param type      type=1表示余额，type=2表示钱包
     * @param linstener {"code":0,"data":{"rate":10}}
     */
    public static void withdrawFeeRate(Context context, String type, final onConnectionFinishLinstener linstener) {
        String token = SPUtils.getString(context, Constant.SP_ACCESS_TOKEN);
        String userId = SPUtils.getString(context, Constant.SP_LOGIN_USERID);
    }


    public static void getNotices(Context context, String pageIndex, String pageRows, final onConnectionFinishLinstener linstener) {
        String token = SPUtils.getString(context, Constant.SP_ACCESS_TOKEN);
        String userId = SPUtils.getString(context, Constant.SP_LOGIN_USERID);
    }

}