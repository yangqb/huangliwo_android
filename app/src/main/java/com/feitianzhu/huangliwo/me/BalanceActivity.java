package com.feitianzhu.huangliwo.me;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.me.ui.AuthEvent;
import com.feitianzhu.huangliwo.me.ui.VerificationActivity2;
import com.feitianzhu.huangliwo.model.BalanceModel;
import com.feitianzhu.huangliwo.model.UserAuth;
import com.feitianzhu.huangliwo.shop.ShopDao;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.ToastUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @class name：com.feitianzhu.fu700.me
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/28 0028 下午 2:22
 */
public class BalanceActivity extends BaseActivity {
    private BalanceModel balanceModel;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.toBeReleased_Amount)
    TextView toBeReleasedAmount;
    @BindView(R.id.tv_profit)
    TextView tvProfit;
    @BindView(R.id.tv_withdrawal)
    TextView tvWithdrawal;
    private String token;
    private String userId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_balance;
    }

    @Override
    protected void initView() {
        titleName.setText("余额");
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        toBeReleasedAmount.setText("");
        tvProfit.setText("");
        tvWithdrawal.setText("");
    }

    @OnClick({R.id.left_button, R.id.btn_withdrawal, R.id.detailed_rules})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.btn_withdrawal://提现
                UserAuth mAuth = Constant.mUserAuth;
                if (null == mAuth || 0 == mAuth.isRnAuth) {
                    //未实名 审核被拒
                    showDialog("你还没有进行实名认证，请先进行实名认证再进行该操作", true);
                } else if (-1 == mAuth.isRnAuth) {
                    showDialog("审核被拒：" + mAuth.rnAuthRefuseReason + ",是否继续进行实名认证", true);
                } else if (mAuth.isRnAuth == 2) {
                    showDialog("你的实名认证正在审核中，请等审核通过后再进行该操作", false);
                } else {
                    //验证用户审核通过
                    intent = new Intent(BalanceActivity.this, WithdrawActivity.class);
                    if(balanceModel == null) {
                        intent.putExtra(WithdrawActivity.BALANCE, 0.00);
                    }else {
                        intent.putExtra(WithdrawActivity.BALANCE, balanceModel.getBalance());
                    }
                    startActivity(intent);
                }
                break;
            case R.id.detailed_rules: //细则
                intent = new Intent(BalanceActivity.this, DetailedRulesActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void showDialog(String result, boolean isGoAuth) {
        new XPopup.Builder(BalanceActivity.this)
                .asConfirm("温馨提示", result, "取消", "确定", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        if (isGoAuth) {
                            Intent mIntent = new Intent(BalanceActivity.this, VerificationActivity2.class);
                            startActivity(mIntent);
                        }
                    }
                }, null, false)
                .bindLayout(R.layout.layout_dialog) //绑定已有布局
                .show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onAuthEvent(AuthEvent mAuth) {
        if (mAuth == AuthEvent.SUCCESS) {
            ShopDao.loadUserAuthImpl(this);
        }
    }

    @Override
    protected void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ShopDao.loadUserAuthImpl(this);

        OkHttpUtils.get()
                .url(Urls.GET_USER_MONEY_INFO)
                .addParams(Constant.ACCESSTOKEN, token)
                .addParams(Constant.USERID, userId)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(String mData, Response response, int id) throws Exception {
                        return new Gson().fromJson(mData, BalanceModel.class);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        balanceModel = (BalanceModel) response;
                        if (balanceModel != null) {
                            String amount = String.format(Locale.getDefault(), "%.2f", balanceModel.getWaitRelease());
                            String amount2 = String.format(Locale.getDefault(), "%.2f", balanceModel.getTotalAmount());
                            String amount3 = String.format(Locale.getDefault(), "%.2f", balanceModel.getBalance());
                            setSpannableString(toBeReleasedAmount, tvProfit, tvWithdrawal, amount, amount2, amount3);
                        } else {
                            setSpannableString(toBeReleasedAmount, tvProfit, tvWithdrawal, "0.00", "0.00", "0.00");
                        }

                    }
                });
    }

    public void setSpannableString(TextView view1, TextView view2, TextView view3, String str1, String str2, String str3) {
        String str0 = "¥ ";
        SpannableString span1 = new SpannableString(str0);
        SpannableString span3 = new SpannableString(str0);
        SpannableString span2 = new SpannableString(str1);
        SpannableString span4 = new SpannableString(str2);
        SpannableString span5 = new SpannableString(str3);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#333333"));
        span1.setSpan(new AbsoluteSizeSpan(15, true), 0, str0.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(new StyleSpan(Typeface.BOLD), 0, str0.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan, 0, str0.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new AbsoluteSizeSpan(12, true), 0, str0.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str0.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan, 0, str0.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        span2.setSpan(new AbsoluteSizeSpan(24, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(new StyleSpan(Typeface.BOLD), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(colorSpan, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        span4.setSpan(new AbsoluteSizeSpan(18, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span4.setSpan(new StyleSpan(Typeface.BOLD), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span4.setSpan(colorSpan, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        span5.setSpan(new AbsoluteSizeSpan(18, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span5.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span5.setSpan(colorSpan, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


        view1.append(span1);
        view1.append(span2);
        view2.append(span3);
        view2.append(span4);
        view3.append(span3);
        view3.append(span5);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
