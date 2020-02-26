package com.feitianzhu.huangliwo.view;

import android.content.Context;
import android.support.annotation.NonNull;

import com.feitianzhu.huangliwo.R;
import com.lxj.xpopup.core.CenterPopupView;

public class CustomTicketPriceDetailView extends CenterPopupView {
    private int type;

    public CustomTicketPriceDetailView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_ticket_price_detail;
    }

    public CustomTicketPriceDetailView setType(int type) {
        this.type = type;
        return this;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
    }
}
