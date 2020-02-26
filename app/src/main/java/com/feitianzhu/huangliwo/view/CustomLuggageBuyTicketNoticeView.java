package com.feitianzhu.huangliwo.view;

import android.content.Context;
import android.support.annotation.NonNull;

import com.feitianzhu.huangliwo.R;
import com.lxj.xpopup.core.CenterPopupView;

public class CustomLuggageBuyTicketNoticeView extends CenterPopupView {
    private int type;

    public CustomLuggageBuyTicketNoticeView(@NonNull Context context) {
        super(context);
    }

    public CustomLuggageBuyTicketNoticeView setType(int type) {
        this.type = type;
        return this;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_luggage_ticket_notice;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
    }
}
