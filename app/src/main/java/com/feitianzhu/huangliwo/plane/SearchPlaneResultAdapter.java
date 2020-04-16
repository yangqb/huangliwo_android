package com.feitianzhu.huangliwo.plane;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MultipleGoSearchFightInfo;
import com.feitianzhu.huangliwo.model.SearchFlightModel;
import com.feitianzhu.huangliwo.utils.DateUtils;
import com.feitianzhu.huangliwo.utils.DoubleUtil;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;

import java.util.List;

public class SearchPlaneResultAdapter extends BaseMultiItemQuickAdapter<MultipleGoSearchFightInfo, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public SearchPlaneResultAdapter(List<MultipleGoSearchFightInfo> data) {
        super(data);
        addItemType(MultipleGoSearchFightInfo.DOMESTIC_TYPE, R.layout.layout_plane_result);
        addItemType(MultipleGoSearchFightInfo.INTERNATIONAL_TYPE, R.layout.layout_plane_result);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultipleGoSearchFightInfo item) {
        helper.addOnClickListener(R.id.ll_rebate);
        MineInfoModel userInfo = UserInfoUtils.getUserInfo(mContext);
        if (userInfo.getAccountType() != 0) {
            helper.setGone(R.id.ll_rebate, false);
            helper.setGone(R.id.vip_rebate, true);
        } else {
            helper.setGone(R.id.ll_rebate, true);
            helper.setGone(R.id.vip_rebate, false);
        }

        if (helper.getItemViewType() == MultipleGoSearchFightInfo.DOMESTIC_TYPE) {
            helper.setText(R.id.arrAirport, item.flightModel.arrAirport + item.flightModel.arrTerminal);
            helper.setText(R.id.dptAirport, item.flightModel.dptAirport + item.flightModel.dptTerminal);
            helper.setText(R.id.dptTime, item.flightModel.dptTime);
            helper.setText(R.id.arrTime, item.flightModel.arrTime);
            helper.setText(R.id.flightTimes, item.flightModel.flightTimes);
            setSpannableString(MathUtils.subZero(String.valueOf(item.flightModel.barePrice)), helper.getView(R.id.barePrice));
            helper.setText(R.id.flightNum, item.flightModel.flightNum + item.flightModel.flightTypeFullName);
            if (item.flightModel.discount != 0) {
                helper.setGone(R.id.discount, true);
                helper.setText(R.id.discount, item.flightModel.discount * 10 + "折");
            } else {
                helper.setGone(R.id.discount, false);
                helper.setText(R.id.discount, "");
            }
            helper.setVisible(R.id.stopCity, false);
            helper.setVisible(R.id.crossDays, false);
            if (item.flightModel.childPrice == 0) {
                helper.setGone(R.id.cprice, false);
            } else {
                helper.setGone(R.id.cprice, true);
                helper.setText(R.id.cprice, "儿童¥" + MathUtils.subZero(String.valueOf(item.flightModel.childPrice)));
            }

            helper.setText(R.id.tv_rebate, "返¥" + MathUtils.subZero(String.valueOf(DoubleUtil.mul(item.flightModel.zk, item.flightModel.barePrice))));
            helper.setText(R.id.vip_rebate, "返¥" + MathUtils.subZero(String.valueOf(DoubleUtil.mul(item.flightModel.zk, item.flightModel.barePrice))));
        } else {
            helper.setText(R.id.tv_rebate, "返¥" + MathUtils.subZero(String.valueOf(DoubleUtil.mul(item.internationalFlightModel.zk, item.internationalFlightModel.price))));
            helper.setText(R.id.vip_rebate, "返¥" + MathUtils.subZero(String.valueOf(DoubleUtil.mul(item.internationalFlightModel.zk, item.internationalFlightModel.price))));
            if (item.internationalFlightModel.goTrip.transitCities != null && item.internationalFlightModel.goTrip.transitCities.size() > 0) {
                helper.setVisible(R.id.stopCity, true);
                setSpannableString2(item.internationalFlightModel.goTrip.transitCities.get(0).transitCityName, helper.getView(R.id.stopCity));
            } else {
                helper.setVisible(R.id.stopCity, false);
            }

            if (item.internationalFlightModel.goTrip.flightSegments.size() > 1) {
                //有中转城市
                helper.setText(R.id.arrAirport, item.internationalFlightModel.goTrip.flightSegments.get(item.internationalFlightModel.goTrip.flightSegments.size() - 1).arrAirportName);
                helper.setText(R.id.arrTime, item.internationalFlightModel.goTrip.flightSegments.get(item.internationalFlightModel.goTrip.flightSegments.size() - 1).arrTime);
                helper.setText(R.id.dptAirport, item.internationalFlightModel.goTrip.flightSegments.get(0).depAirportName);
                helper.setText(R.id.dptTime, item.internationalFlightModel.goTrip.flightSegments.get(0).depTime);
            } else {
                helper.setText(R.id.arrAirport, item.internationalFlightModel.goTrip.flightSegments.get(0).arrAirportName);
                helper.setText(R.id.arrTime, item.internationalFlightModel.goTrip.flightSegments.get(0).arrTime);
                helper.setText(R.id.dptAirport, item.internationalFlightModel.goTrip.flightSegments.get(0).depAirportName);
                helper.setText(R.id.dptTime, item.internationalFlightModel.goTrip.flightSegments.get(0).depTime);
            }


            helper.setText(R.id.flightTimes, DateUtils.minToHour(item.internationalFlightModel.goTrip.duration));
            helper.setText(R.id.flightNum, item.internationalFlightModel.goTrip.flightSegments.get(0).carrierShortName + item.internationalFlightModel.goTrip.flightSegments.get(0).flightNum + item.internationalFlightModel.goTrip.flightSegments.get(0).planeTypeName);
            if (item.internationalFlightModel.goTrip.crossDays == 0) {
                helper.setVisible(R.id.crossDays, false);
            } else {
                helper.setVisible(R.id.crossDays, true);
                helper.setText(R.id.crossDays, "+" + item.internationalFlightModel.goTrip.crossDays + "天");
            }
            if (item.internationalFlightModel.cprice == 0) {
                helper.setGone(R.id.cprice, false);
            } else {
                helper.setText(R.id.cprice, "儿童¥" + MathUtils.subZero(String.valueOf(item.internationalFlightModel.cprice)));
                helper.setGone(R.id.cprice, true);
            }

            if ("economy".equals(item.internationalFlightModel.cabinLevel)) {
                helper.setText(R.id.discount, "经济舱");
                helper.setGone(R.id.discount, true);
            } else if ("first".equals(item.internationalFlightModel.cabinLevel)) {
                helper.setText(R.id.discount, "头等舱");
                helper.setGone(R.id.discount, true);
            } else if ("business".equals(item.internationalFlightModel.cabinLevel)) {
                helper.setText(R.id.discount, "商务舱");
                helper.setGone(R.id.discount, true);
            } else {
                helper.setGone(R.id.discount, false);
            }

            setSpannableString(MathUtils.subZero(String.valueOf(item.internationalFlightModel.price)), helper.getView(R.id.barePrice));
        }
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        String str1 = "¥";
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#FF8300"));
        span1.setSpan(new AbsoluteSizeSpan(15, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#FF8300"));
        span3.setSpan(new AbsoluteSizeSpan(21, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span3);

    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString2(String str3, TextView view) {
        String str1 = "转";
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#28B5FE"));
        span1.setSpan(new AbsoluteSizeSpan(9, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#999999"));
        span3.setSpan(new AbsoluteSizeSpan(9, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.append(span1);
        view.append(span3);

    }
}
