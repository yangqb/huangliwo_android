package com.feitianzhu.fu700.shop.ui;

import android.annotation.SuppressLint;
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
import com.feitianzhu.fu700.me.ui.TotalScoreActivity;
import com.feitianzhu.fu700.payforme.PayForMeEvent;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.feitianzhu.fu700.view.CustomRefundView;
import com.feitianzhu.fu700.vip.CustomPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class EditApplyRefundActivity extends BaseActivity {
    private String[] strings = new String[]{"我不想要了", "选错规格", "填错地址或信息", "其他",};
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.tv_reason)
    TextView tvReason;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    private String str3;
    private String str2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_refund;
    }

    @Override
    protected void initView() {
        titleName.setText("申请退款");
        rightText.setText("提交");
        rightText.setVisibility(View.VISIBLE);
        str2 = "¥ ";
        str3 = String.format(Locale.getDefault(), "%.2f", 188.00);
        setSpannableString();
    }

    @OnClick({R.id.left_button, R.id.rl_reason})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_reason:
                new XPopup.Builder(this)
                        .asCustom(new CustomRefundView(EditApplyRefundActivity.this)
                                .setData(Arrays.asList(strings))
                                .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        tvReason.setText(strings[position]);
                                    }
                                }))
                        .show();
                break;
            case R.id.left_button:
                finish();
                break;
        }

    }


    @Override
    protected void initData() {

    }

    @SuppressLint("SetTextI18n")
    public void setSpannableString() {
        tvPrice.setText("");
        SpannableString span2 = new SpannableString(str2);
        SpannableString span3 = new SpannableString(str3);

        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span2.setSpan(new AbsoluteSizeSpan(13, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(new StyleSpan(Typeface.BOLD), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(colorSpan2, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span3.setSpan(new AbsoluteSizeSpan(18, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


        tvPrice.append(span2);
        tvPrice.append(span3);

    }
}
