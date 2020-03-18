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
import com.feitianzhu.huangliwo.model.MultiGoBackFlightInfo;
import com.feitianzhu.huangliwo.utils.MathUtils;

import java.util.List;

public class SearchBackResultAdapter extends BaseMultiItemQuickAdapter<MultiGoBackFlightInfo, BaseViewHolder> {
    public SearchBackResultAdapter(@Nullable List<MultiGoBackFlightInfo> data) {
        super(data);
        addItemType(MultiGoBackFlightInfo.DOMESTIC, R.layout.layout_plane_result2);
        addItemType(MultiGoBackFlightInfo.INTERNATIONAL, R.layout.layout_plane_result2);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultiGoBackFlightInfo item) {
        if (helper.getItemViewType() == MultiGoBackFlightInfo.DOMESTIC) {
            helper.setText(R.id.depTime, item.domesticFlight.back.depTime);
            helper.setText(R.id.arrTime, item.domesticFlight.back.arrTime);
            helper.setText(R.id.depAirportName, item.domesticFlight.back.depAirport + item.domesticFlight.back.depTerminal);
            helper.setText(R.id.arrAirportName, item.domesticFlight.back.arrAirport + item.domesticFlight.back.arrTerminal);
            helper.setText(R.id.flightNum, item.domesticFlight.back.carrierName + item.domesticFlight.back.flightCode);
            setSpannableString(MathUtils.subZero(String.valueOf(item.domesticFlight.minBarePrice)), helper.getView(R.id.price));
        } else {
            helper.setText(R.id.depTime, item.internationalFlight.backTrip.flightSegments.get(0).depTime);
            helper.setText(R.id.arrTime, item.internationalFlight.backTrip.flightSegments.get(item.internationalFlight.backTrip.flightSegments.size() - 1).arrTime);
            helper.setText(R.id.depAirportName, item.internationalFlight.backTrip.flightSegments.get(0).depAirportName);
            helper.setText(R.id.arrAirportName, item.internationalFlight.backTrip.flightSegments.get(item.internationalFlight.backTrip.flightSegments.size() - 1).arrAirportName);
            helper.setText(R.id.flightNum, item.internationalFlight.backTrip.flightSegments.get(0).carrierShortName + item.internationalFlight.backTrip.flightSegments.get(0).flightNum);
            setSpannableString(MathUtils.subZero(String.valueOf(item.internationalFlight.price)), helper.getView(R.id.price));
        }
        if (item.isSelect) {
            helper.setBackgroundRes(R.id.item, R.color.color_e8f6ff);
        } else {
            helper.setBackgroundRes(R.id.item, R.color.white);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setSpannableString(String str3, TextView view) {
        String str1 = "¥";
        view.setText("");
        SpannableString span1 = new SpannableString(str1);
        SpannableString span2 = new SpannableString("补");
        SpannableString span3 = new SpannableString(str3);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#FF8300"));
        span1.setSpan(new AbsoluteSizeSpan(13, true), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span1.setSpan(colorSpan1, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#FF8300"));
        span2.setSpan(new AbsoluteSizeSpan(14, true), 0, span2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span2.setSpan(colorSpan2, 0, span2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#FF8300"));
        span3.setSpan(new AbsoluteSizeSpan(18, true), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span3.setSpan(colorSpan3, 0, str3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        view.append(span2);
        view.append(span1);
        view.append(span3);
    }
}
