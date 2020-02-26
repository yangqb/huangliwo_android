package com.feitianzhu.huangliwo.view;

import android.content.Context;
import android.support.annotation.NonNull;

import com.feitianzhu.huangliwo.R;
import com.lxj.xpopup.core.CenterPopupView;

public class CustomPlaneInfoView extends CenterPopupView {
    private int type;

    public CustomPlaneInfoView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_plane_info;
    }

    public CustomPlaneInfoView setType(int type) {
        this.type = type;
        return this;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        if (type == 2) {
            findViewById(R.id.tvGoTitle).setVisibility(VISIBLE);
            findViewById(R.id.comePlane).setVisibility(VISIBLE);
            findViewById(R.id.line).setVisibility(VISIBLE);
        } else {
            findViewById(R.id.tvGoTitle).setVisibility(GONE);
            findViewById(R.id.comePlane).setVisibility(GONE);
            findViewById(R.id.line).setVisibility(GONE);
        }
    }
}
