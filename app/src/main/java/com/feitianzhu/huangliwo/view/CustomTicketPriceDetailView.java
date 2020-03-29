package com.feitianzhu.huangliwo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.CustomPriceDetailInfo;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.lxj.xpopup.core.CenterPopupView;

public class CustomTicketPriceDetailView extends CenterPopupView {
    private int type;
    private CustomPriceDetailInfo priceDetailInfo;

    public CustomTicketPriceDetailView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_ticket_price_detail;
    }

    public CustomTicketPriceDetailView setData(CustomPriceDetailInfo priceDetailInfo) {
        this.priceDetailInfo = priceDetailInfo;
        return this;
    }

    public CustomTicketPriceDetailView setType(int type) {
        this.type = type;
        return this;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        TextView goPrice = findViewById(R.id.goPrice);
        TextView gocPrice = findViewById(R.id.gocPrice);
        TextView goArfAndTof = findViewById(R.id.goArfAndTof);
        TextView gocArfAndTof = findViewById(R.id.gocArfAndTof);
        goPrice.setText("¥" + MathUtils.subZero(String.valueOf(priceDetailInfo.price)));
        goArfAndTof.setText("¥" + MathUtils.subZero(String.valueOf(priceDetailInfo.tof + priceDetailInfo.arf)));
        gocPrice.setText("¥" + MathUtils.subZero(String.valueOf(priceDetailInfo.cPrice)));
        gocArfAndTof.setText("¥" + MathUtils.subZero(String.valueOf(priceDetailInfo.tof + priceDetailInfo.arf)));
    }
}
