package com.feitianzhu.huangliwo.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.impl.onConnectionFinishLinstener;
import com.feitianzhu.huangliwo.common.impl.onNetFinishLinstenerT;
import com.feitianzhu.huangliwo.dao.NetworkDao;
import com.feitianzhu.huangliwo.me.ui.ShopShowNoCreateActivity;
import com.feitianzhu.huangliwo.me.ui.VerificationActivity;
import com.feitianzhu.huangliwo.me.ui.VerificationActivity2;
import com.feitianzhu.huangliwo.model.PayInfo;
import com.feitianzhu.huangliwo.model.UserAuth;
import com.feitianzhu.huangliwo.model.WXModel;
import com.feitianzhu.huangliwo.settings.GetPasswordActivity;
import com.feitianzhu.huangliwo.utils.EncryptUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.jungly.gridpasswordview.GridPasswordView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.ref.WeakReference;

import static com.feitianzhu.huangliwo.common.Constant.FailCode;
import static com.feitianzhu.huangliwo.common.Constant.SuccessCode;

/**
 * Created by dicallc on 2017/9/11 0011.
 */

public class ShopHelp {
    private static MaterialDialog mDialog;

    private ShopHelp() {

    }

    /**
     * 验证用户是否实名通过
     * todo 调用此方法之前必须调用一次获取授权接口     ShopDao.loadUserAuthImpl();
     *
     * @param sContext 调用方法activity
     * @param intent   如果实名了跳转的activity
     */
    public static void veriUserJumpActivity(Context sContext, Intent intent) {
        UserAuth mAuth = Constant.mUserAuth;
        if (null == mAuth || 0 == mAuth.isRnAuth) {
            //未实名 审核被拒
            NoUserVeri(sContext, "你还没有进行实名认证，请先进行实名认证再进行该操作");
        } else if (-1 == mAuth.isRnAuth) {
            NoUserVeri(sContext, "审核被拒：" + mAuth.rnAuthRefuseReason + ",是否继续进行实名认证");
        } else if (mAuth.isRnAuth == 2) {
            showVeringDialog(sContext, "你的实名认证正在审核中，请等审核通过后再进行该操作");
        } else {
            //验证用户审核通过
            if (intent != null) {
                sContext.startActivity(intent);
            }
        }
    }

    /**
     * 验证用户是否实名通过+是否设置二次密码
     * todo 调用此方法之前必须调用一次获取授权接口     ShopDao.loadUserAuthImpl();
     *
     * @param activity  调用方法activity
     * @param linstener
     */
    public static void veriUserAndPayPwdJumpActivity(Activity activity, final onConnectionFinishLinstener linstener) {
        UserAuth mAuth = Constant.mUserAuth;
        if (null == mAuth || 0 == mAuth.isRnAuth) {
            //未实名 审核被拒
            NoUserVeri(activity, "你还没有进行实名认证，请先进行实名认证再进行该操作");
        } else if (-1 == mAuth.isRnAuth) {
            NoUserVeri(activity, "审核被拒：" + mAuth.rnAuthRefuseReason + ",是否继续进行实名认证");
        } else if (mAuth.isRnAuth == 2) {
            showVeringDialog(activity, "你的实名认证正在审核中，请等审核通过后再进行该操作");
        } else {
            veriPassword(activity, linstener);
        }
    }

    /**
     * 验证用户是否实名认证和商户认证
     * todo 调用此方法之前必须调用一次获取授权接口     ShopDao.loadUserAuthImpl();
     *
     * @param sContext 调用方法activity
     * @param intent   如果实名和商铺都通过了跳转的activity
     */
    public static void veriUserShopJumpActivity(Context sContext, Intent intent) {
        UserAuth mAuth = Constant.mUserAuth;
        if (null == mAuth || 0 == mAuth.isRnAuth) {
            //未实名 审核被拒
            NoUserVeri(sContext, "你还没有进行实名认证，请先进行实名认证再进行该操作");
        } else if (-1 == mAuth.isRnAuth) {
            NoUserVeri(sContext, "审核被拒：" + mAuth.rnAuthRefuseReason + ",是否继续进行实名认证");
        } else if (mAuth.isRnAuth == 2) {
            showVeringDialog(sContext, "你的实名认证正在审核中，请等审核通过后再进行该操作");
        } else {
            //验证商户审核
            if (0 == mAuth.isMerchantAuth) {
                //没商铺验证，验证没通过
                NoVeriShop(sContext, "你还没有进行商户认证，请先进行商户认证再进行该操作");
            } else if (-1 == mAuth.isMerchantAuth) {
                //被拒绝
                NoVeriShop(sContext, "审核被拒：" + mAuth.merchantAuthRefuseReason + ",是否继续进行店铺认证");
            } else if (mAuth.isMerchantAuth == 2) {
                //审核中
                showVeringDialog(sContext, "你的商户认证正在审核中，请等审核通过后再进行该操作");
            } else {
                //商铺审核通过
                sContext.startActivity(intent);
            }
        }
    }

    /*
     * 实名认证和商户认证都通过了，是否创建过商铺
     * */
    public static void veriJumpActivity(Context sContext) {
        UserAuth mAuth = Constant.mUserAuth;
        if (null == mAuth || 0 == mAuth.isRnAuth) {
            //未实名 审核被拒
            NoUserVeri(sContext, "你还没有进行实名认证，请先进行实名认证再进行该操作");
        } else if (-1 == mAuth.isRnAuth) {
            NoUserVeri(sContext, "审核被拒：" + mAuth.rnAuthRefuseReason + ",是否继续进行实名认证");
        } else if (mAuth.isRnAuth == 2) {
            showVeringDialog(sContext, "你的实名认证正在审核中，请等审核通过后再进行该操作");
        } else {
            //验证商户审核
            if (0 == mAuth.isMerchantAuth) {
                //没商铺验证，验证没通过
                NoVeriShop(sContext, "你还没有进行商户认证，请先进行商户认证再进行该操作");
            } else if (-1 == mAuth.isMerchantAuth) {
                //被拒绝
                NoVeriShop(sContext, "审核被拒：" + mAuth.merchantAuthRefuseReason + ",是否继续进行店铺认证");
            } else if (mAuth.isMerchantAuth == 2) {
                //审核中
                showVeringDialog(sContext, "你的商户认证正在审核中，请等审核通过后再进行该操作");
            } else {
                //验证商铺是否创建
                if (0 == mAuth.isMerchant) {

                    NoCreateShops(sContext, "你还没有创建过商铺，是否创建");
                } else {
                    if (0 == mAuth.isMerchantStatus) {
                        //审核中
                        showVeringDialog(sContext, "你的创建的商铺正在审核中，请等审核通过后再进行该操作");
                    } else if (1 == mAuth.isMerchantStatus) {
                        //有商铺
                        /*Intent mIntent = new Intent(sContext, ShopsActivity.class);
                        mIntent.putExtra(ISADMIN, true);
                        sContext.startActivity(mIntent);*/
                    } else {
                        //拒绝了
                        NoCreateShops(sContext, "审核被拒：" + mAuth.merchantStatusRefuseReason + ",是否继续创建店铺");
                    }
                }
            }
        }
    }

    /*
     * 没有创建商铺弹框
     * */
    private static void NoCreateShops(final Context sContext, String result) {
        if (TextUtils.isEmpty(result)) result = "";
        new XPopup.Builder(sContext)
                .asConfirm("温馨提示", result, "取消", "确定", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        Intent mIntent = new Intent(sContext, ShopShowNoCreateActivity.class);
                        sContext.startActivity(mIntent);
                    }
                }, null, false)
                .bindLayout(R.layout.layout_dialog) //绑定已有布局
                .show();
    }

    /*
     * 没有商户认证弹框
     * */
    private static void NoVeriShop(final Context sContext, String result) {
        if (TextUtils.isEmpty(result)) result = "";
        new XPopup.Builder(sContext)
                .asConfirm("温馨提示", result, "取消", "确定", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        Intent mIntent = new Intent(sContext, VerificationActivity.class);
                        mIntent.putExtra(Constant.VERI_SHOPS, true);
                        sContext.startActivity(mIntent);
                    }
                }, null, false)
                .bindLayout(R.layout.layout_dialog) //绑定已有布局
                .show();
    }

    /*
     * 没有和审核失败实名认证弹框
     * */
    private static void NoUserVeri(final Context sContext, String result) {
        if (TextUtils.isEmpty(result)) result = "";
        new XPopup.Builder(sContext)
                .asConfirm("温馨提示", result, "取消", "确定", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        Intent mIntent = new Intent(sContext, VerificationActivity2.class);
                        sContext.startActivity(mIntent);
                    }
                }, null, false)
                .bindLayout(R.layout.layout_dialog) //绑定已有布局
                .show();
    }

    /*
     * 实名认证和商户认证审核中弹框
     * */
    private static void showVeringDialog(final Context sContext, String content) {
        new XPopup.Builder(sContext)
                .asConfirm("温馨提示", content, "取消", "确定", null, null, false)
                .bindLayout(R.layout.layout_dialog) //绑定已有布局
                .show();
    }

    /*
     * 设置充值提现支付密码
     * */
    public static void showVeringPassword(Activity mWActivity,
                                          final onConnectionFinishLinstener mLinstener) {
        View mView = View.inflate(mWActivity, R.layout.common_password_dialog, null);
        final GridPasswordView mGridPasswordView =
                (GridPasswordView) mView.findViewById(R.id.gpv_normal);
        new MaterialDialog.Builder(mWActivity).title("输入支付密码")
                .customView(mView, false)
                .positiveText("确认")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull final MaterialDialog mMaterialDialog,
                                        @NonNull DialogAction mDialogAction) {
                        if (TextUtils.isEmpty(mGridPasswordView.getPassWord())) {
                            mLinstener.onFail(FailCode, "密码不能为空");
                            return;
                        }
                        final String mPassword = EncryptUtils.encodePassword(mGridPasswordView.getPassWord());
                        NetworkDao.checkPayPwd(mWActivity, mPassword, new onConnectionFinishLinstener() {
                            @Override
                            public void onSuccess(int code, Object result) {
                                mLinstener.onSuccess(SuccessCode, mPassword);
                                mMaterialDialog.dismiss();
                            }

                            @Override
                            public void onFail(int code, String result) {
                                mLinstener.onFail(FailCode, result);
                                mMaterialDialog.dismiss();
                            }
                        });
                        //ToastUtils.showShortToast(mGridPasswordView.getPassWord());
                    }
                })
                .negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog mMaterialDialog,
                                        @NonNull DialogAction mDialogAction) {

                        mMaterialDialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 验证是否设置过二级密码,没有直接跳转设置二级密码
     * * @param mActivity
     */
    public static void veriPassword(Activity mActivity,
                                    final onConnectionFinishLinstener mListener) {
        WeakReference<Activity> mReference = new WeakReference<Activity>(mActivity);
        Activity wActivity = mReference.get();
        if (!Constant.loadUserAuth) {
            ShopDao.loadUserAuthImpl(mActivity);
            ToastUtils.showShortToast("正在获取你的信息，请稍候点击");
        } else {
            if (Constant.mUserAuth.isPaypass == 0) {
                //没有设置二级密码
                ToastUtils.showShortToast("当前没有设置支付密码，请设置后再进行操作");
                GetPasswordActivity.startActivity(mActivity, GetPasswordActivity.TYPE_SET_PAY_PASSWORD_PWD);
            } else {
                showVeringPassword(wActivity, mListener);
            }
        }
    }

    protected static void showloadDialog(String title, Context mContext) {
        mDialog = new MaterialDialog.Builder(mContext)
                .content("加载中,请稍等")
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .show();
    }

    protected static void goneloadDialog() {
        if (null != mDialog && mDialog.isShowing()) if (mDialog.isShowing()) mDialog.dismiss();
    }

    public static void WxPay(String mMoney, final Context mContext) {
        showloadDialog("", mContext);
        ShopDao.postWxPay(mMoney, "wx", new onNetFinishLinstenerT<WXModel>() {
            @Override
            public void onSuccess(int code, WXModel result) {
                Constant.PayFlag = PayInfo.MyMoneyPay;
                IWXAPI api = WXAPIFactory.createWXAPI(mContext, result.appid);
                api.registerApp(result.appid);
                PayReq mPayReq = new PayReq();
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
    }
}
