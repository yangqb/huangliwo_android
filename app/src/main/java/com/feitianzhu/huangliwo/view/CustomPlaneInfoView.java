package com.feitianzhu.huangliwo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.CustomPlaneDetailInfo;
import com.feitianzhu.huangliwo.model.GoBackTripInfo;
import com.feitianzhu.huangliwo.utils.DateUtils;
import com.lxj.xpopup.core.CenterPopupView;

public class CustomPlaneInfoView extends CenterPopupView {
    private int type;
    private CustomPlaneDetailInfo detailInfo;

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

    public CustomPlaneInfoView setData(CustomPlaneDetailInfo detailInfo) {
        this.detailInfo = detailInfo;
        return this;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        TextView tvGoPlaneInfo = findViewById(R.id.tvGoPlaneInfo);
        TextView goBTime = findViewById(R.id.goBTime);
        TextView goDepAirport = findViewById(R.id.goDepAirport);
        TextView goArrAirport = findViewById(R.id.goArrAirport);
        TextView goETime = findViewById(R.id.goETime);
        TextView tvGoCompany = findViewById(R.id.tvGoCompany);
        TextView goFlightTimes = findViewById(R.id.goFlightTimes);

        TextView tvBackPlaneInfo = findViewById(R.id.tvBackPlaneInfo);
        TextView backBTime = findViewById(R.id.backBTime);
        TextView backDepAirport = findViewById(R.id.backDepAirport);
        TextView backArrAirport = findViewById(R.id.backArrAirport);
        TextView backETime = findViewById(R.id.backETime);
        TextView tvBackCompany = findViewById(R.id.tvBackCompany);
        TextView backFlightTimes = findViewById(R.id.backFlightTimes);

        if (type == 2 || type == 3) {
            findViewById(R.id.tvGoTitle).setVisibility(VISIBLE);
            findViewById(R.id.comePlane).setVisibility(VISIBLE);
            findViewById(R.id.line).setVisibility(VISIBLE);
        } else {
            findViewById(R.id.tvGoTitle).setVisibility(GONE);
            findViewById(R.id.comePlane).setVisibility(GONE);
            findViewById(R.id.line).setVisibility(GONE);
        }
        if (type == 0) {
            String date = DateUtils.strToStr(detailInfo.customDocGoFlightInfo.date) + DateUtils.strToDate2(detailInfo.customDocGoFlightInfo.date);
            tvGoPlaneInfo.setText(date + detailInfo.customDocGoFlightInfo.depCode + "-" + detailInfo.customDocGoFlightInfo.arrCode);
            goBTime.setText(detailInfo.customDocGoFlightInfo.btime);
            goDepAirport.setText(detailInfo.customDocGoFlightInfo.depAirport + detailInfo.customDocGoFlightInfo.depTerminal);
            goArrAirport.setText(detailInfo.customDocGoFlightInfo.arrAirport + detailInfo.customDocGoFlightInfo.arrTerminal);
            goETime.setText(detailInfo.customDocGoFlightInfo.etime);
            if (detailInfo.customDocGoFlightInfo.meal) {
                tvGoCompany.setText(detailInfo.customDocGoFlightInfo.com + detailInfo.customDocGoFlightInfo.code + "提供餐食");
            } else {
                tvGoCompany.setText(detailInfo.customDocGoFlightInfo.com + detailInfo.customDocGoFlightInfo.code + "不提供餐食");
            }
            goFlightTimes.setText(detailInfo.customDocGoFlightInfo.flightTimes);
        } else if (type == 1) {
            GoBackTripInfo interGo = detailInfo.customInterFlightInfo.goTrip;
            String date = DateUtils.strToStr(interGo.flightSegments.get(0).depDate) + DateUtils.strToDate2(interGo.flightSegments.get(0).depDate);
            tvGoPlaneInfo.setText(date + interGo.flightSegments.get(0).depCityName + "-" + interGo.flightSegments.get(interGo.flightSegments.size() - 1).arrCityName);
            goBTime.setText(interGo.flightSegments.get(0).depTime);
            goDepAirport.setText(interGo.flightSegments.get(0).depAirportName + interGo.flightSegments.get(0).depTerminal);
            goArrAirport.setText(interGo.flightSegments.get(interGo.flightSegments.size() - 1).arrAirportName + interGo.flightSegments.get(interGo.flightSegments.size() - 1).arrTerminal);
            goETime.setText(interGo.flightSegments.get(interGo.flightSegments.size() - 1).arrTime);
            tvGoCompany.setText(interGo.flightSegments.get(0).carrierShortName + interGo.flightSegments.get(0).flightNum + interGo.flightSegments.get(0).planeTypeName + "不提供餐食");
            goFlightTimes.setText(DateUtils.minToHour(interGo.duration));
        } else if (type == 2) {
            String goDate = DateUtils.strToStr(detailInfo.goDate) + DateUtils.strToDate2(detailInfo.goDate);
            String backDate = DateUtils.strToStr(detailInfo.backDate) + DateUtils.strToDate2(detailInfo.backDate);


            tvGoPlaneInfo.setText(goDate + detailInfo.customDocGoBackFlightInfo.go.depAirportCode + "-" + detailInfo.customDocGoBackFlightInfo.go.arrAirportCode);
            goBTime.setText(detailInfo.customDocGoBackFlightInfo.go.depTime);
            goDepAirport.setText(detailInfo.customDocGoBackFlightInfo.go.depAirport + detailInfo.customDocGoBackFlightInfo.go.depTerminal);
            goArrAirport.setText(detailInfo.customDocGoBackFlightInfo.go.arrAirport + detailInfo.customDocGoBackFlightInfo.go.arrTerminal);
            goETime.setText(detailInfo.customDocGoBackFlightInfo.go.arrTime);
            tvGoCompany.setText(detailInfo.customDocGoBackFlightInfo.go.carrierName + detailInfo.customDocGoBackFlightInfo.go.flightCode);
            goFlightTimes.setText(detailInfo.customDocGoBackFlightInfo.go.flightTimes);


            tvBackPlaneInfo.setText(backDate + detailInfo.customDocGoBackFlightInfo.back.depAirportCode + "-" + detailInfo.customDocGoBackFlightInfo.back.arrAirportCode);
            backBTime.setText(detailInfo.customDocGoBackFlightInfo.back.depTime);
            backDepAirport.setText(detailInfo.customDocGoBackFlightInfo.back.depAirport + detailInfo.customDocGoBackFlightInfo.back.depTerminal);
            backArrAirport.setText(detailInfo.customDocGoBackFlightInfo.back.arrAirport + detailInfo.customDocGoBackFlightInfo.back.arrTerminal);
            backETime.setText(detailInfo.customDocGoBackFlightInfo.back.arrTime);
            tvBackCompany.setText(detailInfo.customDocGoBackFlightInfo.back.carrierName + detailInfo.customDocGoBackFlightInfo.back.flightCode);
            backFlightTimes.setText(detailInfo.customDocGoBackFlightInfo.back.flightTimes);
        } else {
            GoBackTripInfo interGo = detailInfo.customInterFlightInfo.goTrip;
            String goDate = DateUtils.strToStr(interGo.flightSegments.get(0).depDate) + DateUtils.strToDate2(interGo.flightSegments.get(0).depDate);
            tvGoPlaneInfo.setText(goDate + interGo.flightSegments.get(0).depCityName + "-" + interGo.flightSegments.get(interGo.flightSegments.size() - 1).arrCityName);
            goBTime.setText(interGo.flightSegments.get(0).depTime);
            goDepAirport.setText(interGo.flightSegments.get(0).depAirportName + interGo.flightSegments.get(0).depTerminal);
            goArrAirport.setText(interGo.flightSegments.get(interGo.flightSegments.size() - 1).arrAirportName + interGo.flightSegments.get(interGo.flightSegments.size() - 1).arrTerminal);
            goETime.setText(interGo.flightSegments.get(interGo.flightSegments.size() - 1).arrTime);
            tvGoCompany.setText(interGo.flightSegments.get(0).carrierShortName + interGo.flightSegments.get(0).flightNum + interGo.flightSegments.get(0).planeTypeCode + "不提供餐食");
            goFlightTimes.setText(DateUtils.minToHour(interGo.duration));

            GoBackTripInfo interBack = detailInfo.customInterFlightInfo.backTrip;
            String backDate = DateUtils.strToStr(interBack.flightSegments.get(0).depDate) + DateUtils.strToDate2(interBack.flightSegments.get(0).depDate);
            tvBackPlaneInfo.setText(backDate + interBack.flightSegments.get(0).depCityName + "-" + interBack.flightSegments.get(interBack.flightSegments.size() - 1).arrCityName);
            backBTime.setText(interBack.flightSegments.get(0).depTime);
            backDepAirport.setText(interBack.flightSegments.get(0).depAirportName + interBack.flightSegments.get(0).depTerminal);
            backArrAirport.setText(interBack.flightSegments.get(interBack.flightSegments.size() - 1).arrAirportName + interBack.flightSegments.get(interBack.flightSegments.size() - 1).arrTerminal);
            backETime.setText(interBack.flightSegments.get(interBack.flightSegments.size() - 1).arrTime);
            tvBackCompany.setText(interBack.flightSegments.get(0).carrierShortName + interGo.flightSegments.get(0).flightNum + interBack.flightSegments.get(0).planeTypeCode + "不提供餐食");
            backFlightTimes.setText(DateUtils.minToHour(interBack.duration));
        }


    }
}
