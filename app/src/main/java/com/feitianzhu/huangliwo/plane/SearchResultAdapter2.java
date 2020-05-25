package com.feitianzhu.huangliwo.plane;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MultiGoBackFlightInfo;
import com.feitianzhu.huangliwo.utils.DoubleUtil;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;

import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.plane
 * user: yangqinbo
 * date: 2020/3/19
 * time: 9:57
 * email: 694125155@qq.com
 */
public class SearchResultAdapter2 extends BaseMultiItemQuickAdapter<MultiGoBackFlightInfo, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public SearchResultAdapter2(List<MultiGoBackFlightInfo> data) {
        super(data);
        addItemType(MultiGoBackFlightInfo.DOMESTIC, R.layout.layout_search_plane_result2);
        addItemType(MultiGoBackFlightInfo.INTERNATIONAL, R.layout.layout_search_plane_result2);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultiGoBackFlightInfo item) {
        MineInfoModel userInfo = UserInfoUtils.getUserInfo(mContext);
        helper.addOnClickListener(R.id.ll_rebate);
        if (userInfo.getAccountType() != 0) {
            helper.setGone(R.id.ll_rebate, false);
            helper.setGone(R.id.vip_rebate, true);
        } else {
            helper.setGone(R.id.ll_rebate, true);
            helper.setGone(R.id.vip_rebate, false);
        }
        if (helper.getItemViewType() == MultiGoBackFlightInfo.DOMESTIC) {
            helper.setText(R.id.go_depTime, item.domesticFlight.go.depTime);
            helper.setText(R.id.go_arrTime, item.domesticFlight.go.arrTime);
            helper.setText(R.id.go_depAirportName, item.domesticFlight.go.depAirport + item.domesticFlight.go.depTerminal);
            helper.setText(R.id.go_arrAirportName, item.domesticFlight.go.arrAirport + item.domesticFlight.go.arrTerminal);
            helper.setText(R.id.go_flightNum, item.domesticFlight.go.carrierName + item.domesticFlight.go.flightCode);
            helper.setText(R.id.back_depTime, item.domesticFlight.back.depTime);
            helper.setText(R.id.back_arrTime, item.domesticFlight.back.arrTime);
            helper.setText(R.id.back_depAirportName, item.domesticFlight.back.depAirport + item.domesticFlight.back.depTerminal);
            helper.setText(R.id.back_arrAirportName, item.domesticFlight.back.arrAirport + item.domesticFlight.back.arrTerminal);
            helper.setText(R.id.back_flightNum, item.domesticFlight.back.carrierName + item.domesticFlight.back.flightCode);
            helper.setText(R.id.tv_rebate, "奖励¥" + MathUtils.subZero(String.valueOf(DoubleUtil.mul(item.domesticFlight.zk, item.domesticFlight.minBarePrice))));
            helper.setText(R.id.vip_rebate, "奖励¥" + MathUtils.subZero(String.valueOf(DoubleUtil.mul(item.domesticFlight.zk, item.domesticFlight.minBarePrice))));
            setSpannableString(MathUtils.subZero(String.valueOf(item.domesticFlight.minBarePrice)), helper.getView(R.id.price));
        } else {
            helper.setText(R.id.go_depTime, item.internationalFlight.goTrip.flightSegments.get(0).depTime);
            helper.setText(R.id.go_arrTime, item.internationalFlight.goTrip.flightSegments.get(item.internationalFlight.goTrip.flightSegments.size() - 1).arrTime);
            helper.setText(R.id.go_depAirportName, item.internationalFlight.goTrip.flightSegments.get(0).depAirportName);
            helper.setText(R.id.go_arrAirportName, item.internationalFlight.goTrip.flightSegments.get(item.internationalFlight.goTrip.flightSegments.size() - 1).arrAirportName);
            helper.setText(R.id.go_flightNum, item.internationalFlight.goTrip.flightSegments.get(0).carrierShortName + item.internationalFlight.goTrip.flightSegments.get(0).flightNum);
            helper.setText(R.id.back_depTime, item.internationalFlight.backTrip.flightSegments.get(0).depTime);
            helper.setText(R.id.back_arrTime, item.internationalFlight.backTrip.flightSegments.get(item.internationalFlight.backTrip.flightSegments.size() - 1).arrTime);
            helper.setText(R.id.back_depAirportName, item.internationalFlight.backTrip.flightSegments.get(0).depAirportName);
            helper.setText(R.id.back_arrAirportName, item.internationalFlight.backTrip.flightSegments.get(item.internationalFlight.backTrip.flightSegments.size() - 1).arrAirportName);
            helper.setText(R.id.back_flightNum, item.internationalFlight.backTrip.flightSegments.get(0).carrierShortName + item.internationalFlight.backTrip.flightSegments.get(0).flightNum);
            helper.setText(R.id.tv_rebate, "奖励¥" + MathUtils.subZero(String.valueOf(DoubleUtil.mul(item.internationalFlight.zk, item.internationalFlight.price))));
            helper.setText(R.id.vip_rebate, "奖励¥" + MathUtils.subZero(String.valueOf(DoubleUtil.mul(item.internationalFlight.zk, item.internationalFlight.price))));
            setSpannableString(MathUtils.subZero(String.valueOf(item.internationalFlight.price)), helper.getView(R.id.price));
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
}
