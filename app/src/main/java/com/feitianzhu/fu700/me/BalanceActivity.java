package com.feitianzhu.fu700.me;

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

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.shop.ShopDao;
import com.feitianzhu.fu700.shop.ShopHelp;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @class name：com.feitianzhu.fu700.me
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/28 0028 下午 2:22
 */
public class BalanceActivity extends BaseActivity {

    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.toBeReleased_Amount)
    TextView toBeReleasedAmount;
    @BindView(R.id.tv_profit)
    TextView tvProfit;
    @BindView(R.id.tv_withdrawal)
    TextView tvWithdrawal;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_balance;
    }

    @Override
    protected void initView() {
        titleName.setText("余额");

        toBeReleasedAmount.setText("");
        tvProfit.setText("");
        tvWithdrawal.setText("");

        String str1 = "¥ ";
        String str2 = "0.00";
        SpannableString span1 = new SpannableString(str1);
        SpannableString span2 = new SpannableString(str2);
        SpannableString span3 = new SpannableString(str1);
        SpannableString span4 = new SpannableString(str2);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#333333"));
        span1.setSpan(new AbsoluteSizeSpan(15, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(new StyleSpan(Typeface.BOLD), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


        span2.setSpan(new AbsoluteSizeSpan(24, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(new StyleSpan(Typeface.BOLD), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(colorSpan, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        span3.setSpan(new AbsoluteSizeSpan(12, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


        span4.setSpan(new AbsoluteSizeSpan(18, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span4.setSpan(new StyleSpan(Typeface.BOLD), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span4.setSpan(colorSpan, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        toBeReleasedAmount.append(span1);
        toBeReleasedAmount.append(span2);
        tvProfit.append(span3);
        tvProfit.append(span4);
        tvWithdrawal.append(span3);
        tvWithdrawal.append(span4);

    }

    @OnClick({R.id.left_button, R.id.btn_withdrawal, R.id.detailed_rules})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.btn_withdrawal://提现
                intent = new Intent(this, WithdrawActivity.class);
                intent.putExtra(WithdrawActivity.BALANCE, 555.00);
                ShopHelp.veriUserJumpActivity(this, intent);  //是否实名认证
                break;
            case R.id.detailed_rules: //细则
                intent = new Intent(BalanceActivity.this, DetailedRulesActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void initData() {
        ShopDao.loadUserAuthImpl();
    }
}
