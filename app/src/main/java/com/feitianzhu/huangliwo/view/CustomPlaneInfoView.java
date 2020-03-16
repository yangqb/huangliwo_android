package com.feitianzhu.huangliwo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.CustomPlaneFlightInfo;
import com.lxj.xpopup.core.CenterPopupView;

public class CustomPlaneInfoView extends CenterPopupView {
    private int type;
    private CustomPlaneFlightInfo detailInfo;

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

    public CustomPlaneInfoView setData(CustomPlaneFlightInfo detailInfo) {
        this.detailInfo = detailInfo;
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
        TextView tvPlaneInfo = findViewById(R.id.tvPlaneInfo);
        tvPlaneInfo.setText(detailInfo.date + detailInfo.depCity + "-" + detailInfo.arrCity);
        TextView goBTime = findViewById(R.id.goBTime);
        goBTime.setText(detailInfo.bTime);
        TextView goDepAirport = findViewById(R.id.goDepAirport);
        goDepAirport.setText(detailInfo.depAirport + detailInfo.depTerminal);
        TextView goArrAirport = findViewById(R.id.goArrAirport);
        goArrAirport.setText(detailInfo.arrAirport + detailInfo.arrTerminal);
        TextView goETime = findViewById(R.id.goETime);
        goETime.setText(detailInfo.eTime);
        TextView tvCompany = findViewById(R.id.tvCompany);
        if (detailInfo.meal) {
            tvCompany.setText(detailInfo.com + detailInfo.code + "提供餐食");
        } else {
            tvCompany.setText(detailInfo.com + detailInfo.code + "不提供餐食");
        }
        TextView flightTimes = findViewById(R.id.flightTimes);
        flightTimes.setText(detailInfo.flightTime);
    }
}
