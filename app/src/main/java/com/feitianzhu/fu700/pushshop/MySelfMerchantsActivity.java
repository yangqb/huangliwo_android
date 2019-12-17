package com.feitianzhu.fu700.pushshop;

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

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.fu700.pushshop
 * user: yangqinbo
 * date: 2019/12/11
 * time: 15:09
 * email: 694125155@qq.com
 */
public class MySelfMerchantsActivity extends BaseActivity {

    @BindView(R.id.amount)
    TextView tvAmount;
    @BindView(R.id.myMerchant)
    LinearLayout myMerchant;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_myself_merchants;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.transparent)
                .init();
        tvAmount.setText("");
        String str1 = "¥ ";
        String str2 = "0.00";
        SpannableString span1 = new SpannableString(str1);
        SpannableString span2 = new SpannableString(str2);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#333333"));
        span1.setSpan(new AbsoluteSizeSpan(17, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(new StyleSpan(Typeface.BOLD), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


        span2.setSpan(new AbsoluteSizeSpan(26, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(new StyleSpan(Typeface.BOLD), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(colorSpan, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvAmount.append(span1);
        tvAmount.append(span2);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.right_button, R.id.myMerchant})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                break;
            case R.id.myMerchant:
                Intent intent = new Intent(MySelfMerchantsActivity.this, MerchantsDetailActivity.class);
                startActivity(intent);
                break;
        }
    }
}
