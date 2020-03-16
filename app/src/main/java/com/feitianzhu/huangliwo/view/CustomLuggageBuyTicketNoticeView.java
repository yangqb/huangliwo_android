package com.feitianzhu.huangliwo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.BaggageRuleInfo;
import com.lxj.xpopup.core.CenterPopupView;

public class CustomLuggageBuyTicketNoticeView extends CenterPopupView {
    private int type;
    private BaggageRuleInfo baggageRuleInfo;

    public CustomLuggageBuyTicketNoticeView(@NonNull Context context) {
        super(context);
    }

    public CustomLuggageBuyTicketNoticeView setData(BaggageRuleInfo baggageRuleInfo) {
        this.baggageRuleInfo = baggageRuleInfo;
        return this;
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
        if (type == 0 || type == 1) {
            findViewById(R.id.back_bagg).setVisibility(GONE);
            TextView baggText = findViewById(R.id.baggText);
            baggText.setText("手提行李：" + baggageRuleInfo.cabinBaggageRule + ";托运行李：" + baggageRuleInfo.checkedBaggageRule + ";婴儿票行李：" + baggageRuleInfo.infantBaggageRule);
        }
    }
}
