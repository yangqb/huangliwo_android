package com.feitianzhu.huangliwo.pushshop;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.pushshop.bean.SelfMerchantsListInfo;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.XPopup;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

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
    private List<MerchantsModel> merchantsList;
    @BindView(R.id.myMerchantDetail)
    LinearLayout myMerchantDetail;
    @BindView(R.id.merchants_name)
    TextView merchantsName;
    @BindView(R.id.tv_profit)
    TextView tvProfit;
    @BindView(R.id.tv_withdrawal)
    TextView tvWithdrawal;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_myself_merchants;
    }

    @Override
    protected void initView() {
        token = SPUtils.getString(this, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(this, Constant.SP_LOGIN_USERID);
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.transparent)
                .init();
        setSpannableString(tvProfit, tvWithdrawal);
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
                        goneloadDialog();
                    }

                    @Override
                    public void onResponse(SelfMerchantsListInfo response, int id) {
                        goneloadDialog();
                        if (response != null && response.getList().size() > 0) {
                            merchantsList = response.getList();
                            merchantsName.setText(response.getList().get(0).getMerchantName());
                        }
                    }
                });
    }

    @OnClick({R.id.left_button, R.id.right_button, R.id.myMerchantDetail, R.id.merchants_order, R.id.myMerchantList})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
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
                startActivity(intent);
                break;

        }
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
                            }
                        }))
                .show();
    }

    public void setSpannableString(TextView view1, TextView view2) {
        view1.setText("");
        view2.setText("");
        String str1 = "¥ ";
        String str2 = "0.00";
        SpannableString span1 = new SpannableString(str1);
        SpannableString span2 = new SpannableString(str2);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#333333"));
        span1.setSpan(new AbsoluteSizeSpan(16, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(new StyleSpan(Typeface.BOLD), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


        span2.setSpan(new AbsoluteSizeSpan(26, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(new StyleSpan(Typeface.BOLD), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(colorSpan, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        view1.append(span1);
        view1.append(span2);
        view2.append(span1);
        view2.append(span2);

    }
}
