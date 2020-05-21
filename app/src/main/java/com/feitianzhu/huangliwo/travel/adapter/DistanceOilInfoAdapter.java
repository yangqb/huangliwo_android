package com.feitianzhu.huangliwo.travel.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.travel.bean.OilStationsDetailBean;

import java.util.List;

public class DistanceOilInfoAdapter extends BaseQuickAdapter <OilStationsDetailBean.OilInfoBean, BaseViewHolder>{
    private int posion=-1;
    public DistanceOilInfoAdapter(@Nullable List<OilStationsDetailBean.OilInfoBean> data) {
        super(R.layout.popup_item_oil1, data);

    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OilStationsDetailBean.OilInfoBean item) {
            helper.setText(R.id.oilname,item.getOilName());
            if (posion==helper.getAdapterPosition()){
                TextView view = helper.getView(R.id.oilname);
                view.setEnabled(true);
            }else{
                TextView view = helper.getView(R.id.oilname);
                view.setEnabled(false);
            }

    }
    public void chengtextcolor(int posion){
        this.posion=posion;
    }
}
