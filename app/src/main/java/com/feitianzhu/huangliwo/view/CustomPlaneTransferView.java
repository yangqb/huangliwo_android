package com.feitianzhu.huangliwo.view;

import android.content.Context;
import android.support.annotation.NonNull;

import com.feitianzhu.huangliwo.R;
import com.lxj.xpopup.core.CenterPopupView;

public class CustomPlaneTransferView extends CenterPopupView {
    public CustomPlaneTransferView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_plane_transfer;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
    }
}
