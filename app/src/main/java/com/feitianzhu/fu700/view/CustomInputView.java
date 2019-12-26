package com.feitianzhu.fu700.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.feitianzhu.fu700.R;
import com.lxj.xpopup.core.CenterPopupView;

/**
 * package name: com.feitianzhu.fu700.view
 * user: yangqinbo
 * date: 2019/12/22
 * time: 17:12
 * email: 694125155@qq.com
 */
public class CustomInputView extends CenterPopupView {
    private EditText editContent;
    private TextView tvCancel;
    private TextView tvConfirm;

    private OnConfirmClickListener onConfirmClickListener;

    public interface OnConfirmClickListener {
        void onConfirm(String account);
    }

    public CustomInputView setOnConfirmClickListener(OnConfirmClickListener listener) {
        this.onConfirmClickListener = listener;
        return this;
    }

    public CustomInputView(@NonNull Context context) {
        super(context);
        editContent = findViewById(R.id.edit_content);
        tvCancel = findViewById(R.id.tv_cancel);
        tvConfirm = findViewById(R.id.tv_confirm);
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_input;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initListener();
    }

    public CustomInputView setEditHintText(String text) {
        if (editContent != null) {
            editContent.setHint(text);
        }
        return this;
    }

    public CustomInputView setText(int text) {
        if (text != 0) {
            if (editContent != null) {
                editContent.setText(String.valueOf(text));
            }
        }
        return this;
    }

    public void initListener() {
        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tvConfirm.setOnClickListener(new OnClickListener() {
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
