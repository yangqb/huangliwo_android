package com.feitianzhu.huangliwo.pushshop;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.huangliwo.App;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.login.LoginEvent;
import com.feitianzhu.huangliwo.me.WithdrawActivity;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.me.ui.AuthEvent;
import com.feitianzhu.huangliwo.model.UserAuth;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.pushshop.bean.SelfMerchantsListInfo;
import com.feitianzhu.huangliwo.shop.ShopDao;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.XPopup;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * package name: com.feitianzhu.fu700.pushshop
 * user: yangqinbo
 * date: 2019/12/11
 * time: 15:09
 * email: 694125155@qq.com
 */
public class MySelfMerchantsActivity extends BaseActivity {
    private String userId;
    private String token;
    private int selectPos = 0;
    private double balance = 0.00;
    private List<MerchantsModel> merchantsList;
    @BindView(R.id.myMerchantDetail)
    LinearLayout myMerchantDetail;
    @BindView(R.id.merchants_name)
    TextView merchantsName;
    @BindView(R.id.tv_profit)
    TextView tvProfit;
    @BindView(R.id.tv_withdrawal)
    TextView tvWithdrawal;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_myself_merchants;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        ShopDao.loadUserAuthImpl(MySelfMerchantsActivity.this);
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.transparent)
                .init();
        refreshLayout.setEnableLoadMore(false);
        initListener();
    }

    public void initListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });
    }

    @Override
    protected void initData() {
        OkHttpUtils.get()
                .url(Urls.GET_MERCHANTS_LIST)
                .addParams("accessToken", token)
                .addParams("userId", userId)
                .build()
                .execute(new Callback<SelfMerchantsListInfo>() {
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        showloadDialog("");
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        refreshLayout.finishRefresh(false);
                        goneloadDialog();
                    }

                    @Override
                    public void onResponse(SelfMerchantsListInfo response, int id) {
                        refreshLayout.finishRefresh();
                        goneloadDialog();
                        if (response != null) {
                            merchantsList = response.getList();
                            merchantsName.setText(response.getList().get(0).getMerchantName());
                            balance = response.getList().get(0).getBalance();
                            String strIncome = String.format(Locale.getDefault(), "%.2f", response.getList().get(0).getIncome());
                            String strBalance = String.format(Locale.getDefault(), "%.2f", balance);
                            setSpannableString(tvProfit, tvWithdrawal, strIncome, strBalance);
                        }
                    }
                });
    }

    @OnClick({R.id.left_button, R.id.right_button, R.id.myMerchantDetail, R.id.merchants_order, R.id.myMerchantList, R.id.ll_service, R.id.detailed_rules, R.id.btn_withdrawal})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                intent = new Intent(MySelfMerchantsActivity.this, MerchantsPaymentCodeActivity.class);
                intent.putExtra(MerchantsPaymentCodeActivity.MERCHANTS_ID, merchantsList.get(selectPos).getMerchantId());
                startActivity(intent);
                break;
            case R.id.myMerchantList:
                if (merchantsList != null && merchantsList.size() > 0) {
                    showMerchantsList();
                }
                break;
            case R.id.myMerchantDetail:
                intent = new Intent(MySelfMerchantsActivity.this, MySelfMerchantsListActivity.class);
                intent.putExtra(MySelfMerchantsListActivity.MERCHANTS_ID, merchantsList.get(selectPos).getMerchantId());
                startActivity(intent);
                break;
            case R.id.merchants_order:
                intent = new Intent(MySelfMerchantsActivity.this, MySelfMerchantsOrderActivity.class);
                intent.putExtra(MySelfMerchantsOrderActivity.MERCHANTS_ID, merchantsList.get(selectPos).getMerchantId());
                startActivity(intent);
                break;
            case R.id.ll_service:
                startActivity(new Intent(MySelfMerchantsActivity.this, ProblemFeedbackActivity.class));
                break;
            case R.id.btn_withdrawal:
                UserAuth mAuth = Constant.mUserAuth;
                if (null == mAuth || 0 == mAuth.isRnAuth) {
                    //未实名 审核被拒
                    showDialog("您还没有进行实名认证，请先进行实名认证再进行该操作", false);
                } else if (-1 == mAuth.isRnAuth) {
                    showDialog("您的实名认证审核被拒：请重新进行实名认证", false);
                } else if (mAuth.isRnAuth == 2) {
                    showDialog("您的实名认证正在审核中，请等审核通过后再进行该操作", false);
                } else {
                    //验证用户审核通过
                    intent = new Intent(MySelfMerchantsActivity.this, WithdrawActivity.class);
                    intent.putExtra(WithdrawActivity.BALANCE, balance);
                    intent.putExtra(WithdrawActivity.MERCHANT_ID, merchantsList.get(selectPos).getMerchantId());
                    startActivity(intent);
                }
                break;
            case R.id.detailed_rules:
                intent = new Intent(MySelfMerchantsActivity.this, MyMerchantsEarningsRulesActivity.class);
                intent.putExtra(MyMerchantsEarningsRulesActivity.MERCHANTS_ID, merchantsList.get(selectPos).getMerchantId());
                startActivity(intent);
                break;
        }
    }

    public void showDialog(String result, boolean isGoAuth) {
        new XPopup.Builder(MySelfMerchantsActivity.this)
                .asConfirm("温馨提示", result, "取消", "确定", null, null, false)
                .bindLayout(R.layout.layout_dialog) //绑定已有布局
                .show();
    }

    public void showMerchantsList() {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < merchantsList.size(); i++) {
            stringList.add(merchantsList.get(i).getMerchantName());
        }
        new XPopup.Builder(this)
                .asCustom(new CustomRefundView(MySelfMerchantsActivity.this)
                        .setData(stringList)
                        .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                selectPos = position;
                                merchantsName.setText(merchantsList.get(position).getMerchantName());
                                balance = merchantsList.get(position).getBalance();
                                String strIncome = String.format(Locale.getDefault(), "%.2f", merchantsList.get(position).getIncome());
                                String strBalance = String.format(Locale.getDefault(), "%.2f", balance);
                                setSpannableString(tvProfit, tvWithdrawal, strIncome, strBalance);
                            }
                        }))
                .show();
    }

    private void requestPermission() {
        AndPermission.with(this)
                .requestCode(200)
                .permission(
                        // 多个权限，以数组的形式传入。
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .callback(
                        new RationaleListener() {
                            @Override
                            public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                // 此对话框可以自定义，调用rationale.resume()就可以继续申请。
                                AndPermission.rationaleDialog(App.getAppContext(), rationale).show();
                            }
                        }
                )
                .callback(listener)
                .start();
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。

            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if (requestCode == 200) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + Constant.CUSTOMER_SERVICE_TELEPHONE));
                startActivity(intent);
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 200) {
                Toast.makeText(MySelfMerchantsActivity.this, "请求权限失败!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void setSpannableString(TextView view1, TextView view2, String income, String balance) {
        view1.setText("");
        view2.setText("");
        String str1 = "¥ ";
        SpannableString span1 = new SpannableString(str1);
        SpannableString span2 = new SpannableString(income);
        SpannableString span3 = new SpannableString(balance);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#333333"));
        span1.setSpan(new AbsoluteSizeSpan(16, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(new StyleSpan(Typeface.BOLD), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


        span2.setSpan(new AbsoluteSizeSpan(26, true), 0, income.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(new StyleSpan(Typeface.BOLD), 0, income.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(colorSpan, 0, income.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        span3.setSpan(new AbsoluteSizeSpan(26, true), 0, balance.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, balance.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan, 0, balance.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view1.append(span1);
        view1.append(span2);
        view2.append(span1);
        view2.append(span3);

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onAuthEvent(AuthEvent mAuth) {
        if (mAuth == AuthEvent.SUCCESS) {
            ShopDao.loadUserAuthImpl(MySelfMerchantsActivity.this);  //实名认证后更新用户认证信息
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
