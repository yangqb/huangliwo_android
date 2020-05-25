package com.feitianzhu.huangliwo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.utils.doubleclick.SingleClick;
import com.lxj.xpopup.core.CenterPopupView;

/**
 * package name: com.feitianzhu.huangliwo.view
 * user: yangqinbo
 * date: 2020/4/19
 * time: 15:49
 * email: 694125155@qq.com
 */
public class CustomVipInputView extends CenterPopupView {
    private EditText editContent;
    private TextView tvCancel;
    private TextView tvConfirm;
    private TextView tvTitle;
    private int text;
    private String hintText;
    private String titleText;

    private CustomVipInputView.OnConfirmClickListener onConfirmClickListener;

    public interface OnConfirmClickListener {
        void onConfirm(String account);
    }

    public CustomVipInputView setOnConfirmClickListener(CustomVipInputView.OnConfirmClickListener listener) {
        this.onConfirmClickListener = listener;
        return this;
    }

    public CustomVipInputView(@NonNull Context context) {
        super(context);
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_input;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        editContent = findViewById(R.id.edit_content);
        tvCancel = findViewById(R.id.tv_cancel);
        tvConfirm = findViewById(R.id.tv_confirm);
        tvTitle = findViewById(R.id.tvTitle);
        if (text != 0) {
            editContent.setText(String.valueOf(text));
        } else {
            if (hintText != null) {
                editContent.setHint(hintText);
            }
        }
        if (titleText == null) {
            tvTitle.setVisibility(GONE);
            findViewById(R.id.line).setVisibility(GONE);
        } else {
            tvTitle.setVisibility(VISIBLE);
            findViewById(R.id.line).setVisibility(VISIBLE);
            tvTitle.setText(titleText);
        }
        initListener();
    }

    public CustomVipInputView setEditHintText(String hintText) {
        this.hintText = hintText;
        return this;
    }

    public CustomVipInputView setText(int text) {
        this.text = text;
        return this;
    }

    public CustomVipInputView setTitle(String titleText) {
        this.titleText = titleText;
        return this;
    }

    public void initListener() {

        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tvConfirm.setOnClickListener(new OnClickListener() {
            @SingleClick()
            @Override
            public void onClick(View v) {
                if (onConfirmClickListener != null) {
                    onConfirmClickListener.onConfirm(editContent.getText().toString().trim());
                    if (!TextUtils.isEmpty(editContent.getText().toString().trim())) {
                        dismiss();
                    }
                }
            }
        });
    }
}
