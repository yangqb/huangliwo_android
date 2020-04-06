package com.feitianzhu.huangliwo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.BaggageRuleInfo;
import com.lxj.xpopup.core.CenterPopupView;

public class CustomLuggageBuyTicketNoticeView extends CenterPopupView {
    private int type;
    private BaggageRuleInfo goBaggageRuleInfo;
    private BaggageRuleInfo backBaggageRuleInfo;

    public CustomLuggageBuyTicketNoticeView(@NonNull Context context) {
        super(context);
    }

    public CustomLuggageBuyTicketNoticeView setBackData(BaggageRuleInfo baggageRuleInfo) {
        this.backBaggageRuleInfo = baggageRuleInfo;
        return this;
    }

    public CustomLuggageBuyTicketNoticeView setGoData(BaggageRuleInfo baggageRuleInfo) {
        this.goBaggageRuleInfo = baggageRuleInfo;
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
            findViewById(R.id.go_tag).setVisibility(GONE);
        } else {
            findViewById(R.id.back_bagg).setVisibility(VISIBLE);
            findViewById(R.id.go_tag).setVisibility(VISIBLE);
        }
        TextView btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        TextView go_baggText = findViewById(R.id.go_baggText);
        TextView back_baggText = findViewById(R.id.back_baggText);
        if (goBaggageRuleInfo != null) {
            go_baggText.setText("手提行李：" + goBaggageRuleInfo.cabinBaggageRule + ";托运行李：" + goBaggageRuleInfo.checkedBaggageRule + ";婴儿票行李：" + goBaggageRuleInfo.infantBaggageRule);
        }
        if (backBaggageRuleInfo != null) {
            back_baggText.setText("手提行李：" + goBaggageRuleInfo.cabinBaggageRule + ";托运行李：" + goBaggageRuleInfo.checkedBaggageRule + ";婴儿票行李：" + goBaggageRuleInfo.infantBaggageRule);
        }
    }
}
