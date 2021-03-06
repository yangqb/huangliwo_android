package com.feitianzhu.huangliwo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.LazyWebActivity;
import com.feitianzhu.huangliwo.utils.Urls;
import com.lxj.xpopup.core.CenterPopupView;

public class CustomUserPrivateView extends CenterPopupView {
    private String titleName;
    private String content;
    private Context context;
    private OnClickCancelListener onClickCancelListener;
    private OnClickConfirmListener onClickConfirmListener;

    public interface OnClickCancelListener {
        void onCancel();
    }

    public interface OnClickConfirmListener {
        void onConfirm();
    }


    public CustomUserPrivateView(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public CustomUserPrivateView setOnClickCancelListener(OnClickCancelListener onClickCancelListener) {
        this.onClickCancelListener = onClickCancelListener;
        return this;
    }

    public CustomUserPrivateView setOnClickConfirmListener(OnClickConfirmListener onClickConfirmListener) {
        this.onClickConfirmListener = onClickConfirmListener;
        return this;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_dialog_login;
    }

    /*public CustomUserPrivateView setTitle(String title) {
        this.titleName = title;
        return this;
    }

    public CustomUserPrivateView setContent(String content) {
        this.content = content;
        return this;
    }*/

    @Override
    protected void onCreate() {
        super.onCreate();
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvContent = findViewById(R.id.tv_content);
        TextView tvCancel = findViewById(R.id.tv_cancel);
        TextView tvConfirm = findViewById(R.id.tv_confirm);
        tvCancel.setText("暂不使用");
        tvConfirm.setText("确定");
        tvConfirm.setTextColor(context.getResources().getColor(R.color.color_289cef));
        tvContent.setGravity(Gravity.START);
        tvTitle.setText("服务协议和隐私政策");
        String str1 = "请您务必审慎阅读，充分理解“服务协议”和“隐私政策”各条款，包括但不限于：为了向您提供购物，分享等服务，我们需收集您的设备信息、操作日志等个人信息。您可以在“设置”中查看、变更、删除个人信息并管理您的授权。\n" +
                "您可阅读";
        String str2 = "《服务协议》";
        String str3 = "和";
        String str4 = "《隐私政策》";
        String str5 = "了解详细信息。如您同意，请点击“同意”开始接受我们的服务。";
        setSpannableString(str1, str2, str3, str4, str5, tvContent);
        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickCancelListener != null) {
                    onClickCancelListener.onCancel();
                    dismiss();
                }
            }
        });

        tvConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickConfirmListener != null) {
                    onClickConfirmListener.onConfirm();
                    dismiss();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str1, String str2, String str3, String str4, String str5, TextView tvContent) {
        tvContent.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span2 = new SpannableString(str2);
        SpannableString span3 = new SpannableString(str3);
        SpannableString span4 = new SpannableString(str4);
        SpannableString span5 = new SpannableString(str5);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#333333"));
        span1.setSpan(new AbsoluteSizeSpan(11, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#289cef"));
        span2.setSpan(new AbsoluteSizeSpan(11, true), 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(colorSpan2, 0, str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if (widget instanceof TextView) {
                    ((TextView) widget).setHighlightColor(Color.TRANSPARENT);
                }

                Intent intent = new Intent(context, LazyWebActivity.class);
                intent.putExtra(Constant.URL, Urls.BASE_URL + "fhwl/static/html/fuwuxieyi.html");
                intent.putExtra(Constant.H5_TITLE, "便利大本营用户服务协议");
                context.startActivity(intent);
            }

            //去除连接下划线
            @Override
            public void updateDrawState(TextPaint ds) {
                /**set textColor**/
                ds.setColor(ds.linkColor);
                /**Remove the underline**/
                ds.setUnderlineText(false);
                ds.clearShadowLayer();
            }
        }, 0, span2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        span3.setSpan(new AbsoluteSizeSpan(11, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan1, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


        span4.setSpan(new AbsoluteSizeSpan(11, true), 0, str4.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span4.setSpan(colorSpan2, 0, str4.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span4.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if (widget instanceof TextView) {
                    ((TextView) widget).setHighlightColor(Color.TRANSPARENT);
                }
                Intent intent = new Intent(context, LazyWebActivity.class);
                intent.putExtra(Constant.URL, Urls.BASE_URL + "fhwl/static/html/yinsishuoming.html");
                intent.putExtra(Constant.H5_TITLE, "便利大本营用户隐私说明");
                context.startActivity(intent);
            }

            //去除连接下划线
            @Override
            public void updateDrawState(TextPaint ds) {
                /**set textColor**/
                ds.setColor(ds.linkColor);
                /**Remove the underline**/
                ds.setUnderlineText(false);
                ds.clearShadowLayer();
            }
        }, 0, span4.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        span5.setSpan(new AbsoluteSizeSpan(11, true), 0, str5.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span5.setSpan(colorSpan1, 0, str5.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        tvContent.append(span1);
        tvContent.append(span2);
        tvContent.append(span3);
        tvContent.append(span4);
        tvContent.append(span5);
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
