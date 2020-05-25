package com.feitianzhu.huangliwo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.lxj.xpopup.core.BottomPopupView;

/**
 * package name: com.feitianzhu.huangliwo.view
 * user: yangqinbo
 * date: 2020/4/3
 * time: 9:44
 * email: 694125155@qq.com
 */
public class CustomPayView extends BottomPopupView implements View.OnClickListener {
    private RelativeLayout btnCancel;
    private TextView btnConfirm;
    private ImageView weixinPayIcon;
    private ImageView alipayIcon;
    private String payChannel = "alipay";
    private ImageView balancePayIcon;
    private String data;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weixinPay_icon:
                payChannel = "wx";
                weixinPayIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
                alipayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                balancePayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                break;
            case R.id.alipay_icon:
                payChannel = "alipay";
                weixinPayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                alipayIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
                balancePayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                break;
            case R.id.balancePay_icon:
                weixinPayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                alipayIcon.setBackgroundResource(R.mipmap.e01_24weixuanzhong);
                balancePayIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
                payChannel = "balance";
                break;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String payChannel);
    }

    private CustomPayView.OnItemClickListener mOnConfirmClickListener;

    public CustomPayView setOnConfirmClickListener(CustomPayView.OnItemClickListener onConfirmClickListener) {
        this.mOnConfirmClickListener = onConfirmClickListener;
        return this;
    }

    public CustomPayView setData(String data) {
        this.data = data;
        return this;
    }

    public CustomPayView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_pay;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        btnCancel = findViewById(R.id.btn_cancel);
        btnConfirm = findViewById(R.id.btn_confirm);
        weixinPayIcon = findViewById(R.id.weixinPay_icon);
        alipayIcon = findViewById(R.id.alipay_icon);
        balancePayIcon = findViewById(R.id.balancePay_icon);
        alipayIcon.setBackgroundResource(R.mipmap.e01_23xuanzhong);
        weixinPayIcon.setOnClickListener(this);
        alipayIcon.setOnClickListener(this);
        balancePayIcon.setOnClickListener(this);
        TextView price = findViewById(R.id.price);
        setSpannableString(MathUtils.subZero(data), price);

        initListener();
    }

    public void initListener() {
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnConfirmClickListener != null) {
                    mOnConfirmClickListener.onItemClick(payChannel);
                    dismiss();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        String str1 = "¥";
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span1.setSpan(new AbsoluteSizeSpan(15, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#F88D03"));
        span3.setSpan(new AbsoluteSizeSpan(21, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span3);

    }
}
