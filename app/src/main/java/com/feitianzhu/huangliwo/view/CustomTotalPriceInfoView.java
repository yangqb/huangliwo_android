package com.feitianzhu.huangliwo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.CustomPriceDetailInfo;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.lxj.xpopup.core.BottomPopupView;

/**
 * package name: com.feitianzhu.huangliwo.view
 * user: yangqinbo
 * date: 2020/3/13
 * time: 18:17
 * email: 694125155@qq.com
 */
public class CustomTotalPriceInfoView extends BottomPopupView {
    private int type;
    private CustomPriceDetailInfo priceDetailInfo;

    public CustomTotalPriceInfoView(@NonNull Context context) {
        super(context);
    }

    public CustomTotalPriceInfoView setType(int type) {
        this.type = type;
        return this;
    }

    public CustomTotalPriceInfoView setData(CustomPriceDetailInfo priceDetailInfo) {
        this.priceDetailInfo = priceDetailInfo;
        return this;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_dialog_total_price;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        if (type == 0 || type == 1) {
            findViewById(R.id.go_title).setVisibility(GONE);
            findViewById(R.id.ll_back).setVisibility(GONE);
            TextView goPrice = findViewById(R.id.go_price);
            goPrice.setText("¥" + MathUtils.subZero(String.valueOf(priceDetailInfo.price)) + "x" + priceDetailInfo.num);
            TextView goArfTof = findViewById(R.id.go_arfTof);
            goArfTof.setText("¥" + MathUtils.subZero(String.valueOf(priceDetailInfo.arf + priceDetailInfo.tof)) + "x" + priceDetailInfo.num);
            TextView goCprice = findViewById(R.id.go_cprice);
            TextView goCArfTof = findViewById(R.id.go_cArfTof);
            goCprice.setText("¥" + MathUtils.subZero(String.valueOf(priceDetailInfo.cPrice)) + "x" + priceDetailInfo.cnum);
            goCArfTof.setText("¥" + MathUtils.subZero(String.valueOf(priceDetailInfo.arf + priceDetailInfo.tof)) + "x" + priceDetailInfo.cnum);
        }


    }
}
