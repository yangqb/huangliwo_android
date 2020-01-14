package com.feitianzhu.huangliwo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.utils.EditTextUtils;
import com.lxj.xpopup.core.CenterPopupView;

/**
 * package name: com.feitianzhu.huangliwo.view
 * user: yangqinbo
 * date: 2020/1/13
 * time: 20:18
 * email: 694125155@qq.com
 */
public class AddSetMealView extends CenterPopupView {
    private TextView tvCancel;
    private TextView tvConfirm;
    private EditText editName;
    private EditText editNum;
    private EditText editPrice;
    private OnConfirmClickListener onConfirmClickListener;

    public interface OnConfirmClickListener {
        void onConfirm(String name, String num, String price);
    }

    public AddSetMealView setOnConfirmClickListener(OnConfirmClickListener listener) {
        this.onConfirmClickListener = listener;
        return this;
    }

    public AddSetMealView(@NonNull Context context) {
        super(context);
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_add_setmeal_dialog;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        editName = findViewById(R.id.editName);
        editNum = findViewById(R.id.editNum);
        editPrice = findViewById(R.id.editPrice);
        tvCancel = findViewById(R.id.tv_cancel);
        tvConfirm = findViewById(R.id.tv_confirm);
        EditTextUtils.afterDotTwo(editPrice);
        initListener();
    }

    public void initListener() {
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
                    onConfirmClickListener.onConfirm(editName.getText().toString().trim(), editNum.getText().toString().trim(), editPrice.getText().toString().trim());
                    if (!TextUtils.isEmpty(editName.getText().toString().trim()) && !TextUtils.isEmpty(editNum.getText().toString().trim()) && !TextUtils.isEmpty(editPrice.getText().toString().trim())) {
                        dismiss();
                    }
                }
            }
        });
    }
}
