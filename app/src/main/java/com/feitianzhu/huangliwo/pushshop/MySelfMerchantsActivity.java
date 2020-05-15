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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.huangliwo.App;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.LazyWebActivity;
import com.feitianzhu.huangliwo.http.JsonCallback;
import com.feitianzhu.huangliwo.http.LzyResponse;
import com.feitianzhu.huangliwo.me.WithdrawActivity;
import com.feitianzhu.huangliwo.me.WithdrawRecordActivity;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.me.ui.AuthEvent;
import com.feitianzhu.huangliwo.model.UserAuth;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.pushshop.bean.SelfMerchantsListInfo;
import com.feitianzhu.huangliwo.shop.ShopDao;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomClassificationView;
import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.XPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import q.rorbin.badgeview.QBadgeView;

/**
 * package name: com.feitianzhu.fu700.pushshop
 * user: yangqinbo
 * date: 2019/12/11
 * time: 15:09
 * email: 694125155@qq.com
 */
public class MySelfMerchantsActivity extends BaseActivity {
    public static final int REQUEST_CODE = 100;
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
    RefreshLayout refreshLayout;
    @BindView(R.id.imgView)
    ImageView imgView;
    @BindView(R.id.merchants_order)
    LinearLayout llMerchantsOrder;
    @BindView(R.id.withdrawCount)
    TextView withdrawCount;


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

        OkGo.<LzyResponse<SelfMerchantsListInfo>>get(Urls.GET_MERCHANTS_LIST)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .execute(new JsonCallback<LzyResponse<SelfMerchantsListInfo>>() {
                    @Override
                    public void onStart(com.lzy.okgo.request.base.Request<LzyResponse<SelfMerchantsListInfo>, ? extends com.lzy.okgo.request.base.Request> request) {
                        super.onStart(request);
                        showloadDialog("");
                    }

                    @Override
                    public void onSuccess(Response<LzyResponse<SelfMerchantsListInfo>> response) {
                        super.onSuccess(MySelfMerchantsActivity.this, response.body().msg, response.body().code);
                        refreshLayout.finishRefresh();
                        goneloadDialog();
                        if (response.body().data != null && response.body().code == 0) {
                            if (response.body().data.getList() != null && response.body().data.getList().size() > 0) {
                                merchantsList = response.body().data.getList();
                                merchantsName.setText(response.body().data.getList().get(0).getMerchantName());
                                balance = response.body().data.getList().get(0).getBalance();
                                String strIncome = String.format(Locale.getDefault(), "%.2f", response.body().data.getList().get(0).getIncome());
                                String strBalance = String.format(Locale.getDefault(), "%.2f", balance);
                                if (response.body().data.getList().get(0).getWithdrawCount() == 0) {
                                    withdrawCount.setVisibility(View.INVISIBLE);
                                } else {
                                    withdrawCount.setText(response.body().data.getList().get(0).getWithdrawCount() + "笔正在提现中");
                                    withdrawCount.setVisibility(View.VISIBLE);
                                }
                                setSpannableString(tvProfit, tvWithdrawal, strIncome, strBalance);
                                getUnConsumeCount();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<SelfMerchantsListInfo>> response) {
                        super.onError(response);
                        refreshLayout.finishRefresh(false);
                        goneloadDialog();
                    }
                });
    }

    public void getUnConsumeCount() {
        OkGo.<LzyResponse<Integer>>get(Urls.GET_UNCONSUME_ORDER)
                .tag(this)
                .params("accessToken", token)
                .params("userId", userId)
                .params("merchantId", merchantsList.get(selectPos).getMerchantId() + "")
                .execute(new JsonCallback<LzyResponse<Integer>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<Integer>> response) {
                        if (response.body().code == 0) {
                            new QBadgeView(MySelfMerchantsActivity.this)
                                    .bindTarget(llMerchantsOrder).setGravityOffset(15, 15, true)
                                    .setBadgeNumber(response.body().data);
                                    //.setBadgeNumber(18);

                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<Integer>> response) {
                        super.onError(response);
                    }
                });


    }

    @OnClick({R.id.left_button, R.id.right_button, R.id.myMerchantDetail, R.id.merchants_order, R.id.myMerchantList, R.id.feedback, R.id.detailed_rules, R.id.btn_withdrawal, R.id.protocol, R.id.withdrawCount
            , R.id.up_SetMeal, R.id.up_gift})
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
            case R.id.up_gift:
                intent = new Intent(MySelfMerchantsActivity.this, UpMerchantsGiftActivity.class);
                intent.putExtra(UpMerchantsGiftActivity.MERCHANTS_ID, merchantsList.get(selectPos).getMerchantId());
                startActivity(intent);
                break;
            case R.id.up_SetMeal:
                intent = new Intent(MySelfMerchantsActivity.this, SetMealListActivity.class);
                intent.putExtra(SetMealListActivity.MERCHANTS_ID, merchantsList.get(selectPos).getMerchantId());
                startActivity(intent);
                break;
            /*case R.id.ll_investment:
                ToastUtils.show("敬请期待");
                break;
            case R.id.ll_propaganda:
                ToastUtils.show("敬请期待");
                break;*/
            case R.id.feedback:
                startActivity(new Intent(MySelfMerchantsActivity.this, ProblemFeedbackActivity.class));
                break;
            case R.id.withdrawCount:
                intent = new Intent(MySelfMerchantsActivity.this, WithdrawRecordActivity.class);
                intent.putExtra(WithdrawRecordActivity.MERCHANT_ID, merchantsList.get(selectPos).getMerchantId());
                startActivity(intent);
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
                    startActivityForResult(intent, REQUEST_CODE);
                }
                break;
            case R.id.detailed_rules:
                intent = new Intent(MySelfMerchantsActivity.this, MyMerchantsEarningsRulesActivity.class);
                intent.putExtra(MyMerchantsEarningsRulesActivity.MERCHANTS_ID, merchantsList.get(selectPos).getMerchantId());
                startActivity(intent);
                break;
            case R.id.protocol:
                intent = new Intent(MySelfMerchantsActivity.this, LazyWebActivity.class);
                intent.putExtra(Constant.URL, Urls.BASE_URL + "fhwl/static/html/tuidianxieyi.html");
                intent.putExtra(Constant.H5_TITLE, "便利大本营推店协议");
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
                .asCustom(new CustomClassificationView(MySelfMerchantsActivity.this)
                        .setData(stringList)
                        .setOnItemClickListener(new CustomClassificationView.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                selectPos = position;
                                merchantsName.setText(merchantsList.get(position).getMerchantName());
                                balance = merchantsList.get(position).getBalance();
                                if (merchantsList.get(position).getWithdrawCount() == 0) {
                                    withdrawCount.setVisibility(View.INVISIBLE);
                                } else {
                                    withdrawCount.setText(merchantsList.get(position).getWithdrawCount() + "笔正在提现中");
                                    withdrawCount.setVisibility(View.VISIBLE);
                                }
                                String strIncome = String.format(Locale.getDefault(), "%.2f", merchantsList.get(position).getIncome());
                                String strBalance = String.format(Locale.getDefault(), "%.2f", balance);
                                setSpannableString(tvProfit, tvWithdrawal, strIncome, strBalance);
                                getUnConsumeCount();
                            }
                        }))
                .show();
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                initData();
            }
        }
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