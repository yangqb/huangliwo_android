package com.feitianzhu.huangliwo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.lxj.xpopup.core.CenterPopupView;

/**
 * package name: com.feitianzhu.huangliwo.view
 * user: yangqinbo
 * date: 2020/1/4
 * time: 16:40
 * email: 694125155@qq.com
 */
public class CustomVerificationView extends CenterPopupView {

    private OnConfirmListener onConfirmListener;
    private String content = "";

    public interface OnConfirmListener {
        void onConfirm();
    }

    public CustomVerificationView setOnConfirmListener(OnConfirmListener listener) {
        this.onConfirmListener = listener;
        return this;
    }

    public CustomVerificationView(@NonNull Context context) {
        super(context);
    }

    public CustomVerificationView setContent(String content) {
        if (content != null) {
            this.content = content;
        }
        return this;
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_verification;
    }

    // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.submit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onConfirmListener != null) {
                    onConfirmListener.onConfirm();
                    dismiss();
                }
            }
        });
        TextView tvContent = findViewById(R.id.content);
        tvContent.setText(content);
    }
}
