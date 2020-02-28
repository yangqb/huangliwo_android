package com.feitianzhu.huangliwo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.lxj.xpopup.core.CenterPopupView;

public class CustomPassengerNameView extends CenterPopupView {
    private String mContent;

    public CustomPassengerNameView(@NonNull Context context) {
        super(context);
    }

    public CustomPassengerNameView setContent(String content) {
        this.mContent = content;
        return this;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_passenger_name;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        TextView tvContent = findViewById(R.id.tvContent);
        tvContent.setText(mContent);
    }
}
