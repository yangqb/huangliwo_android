package com.feitianzhu.huangliwo.travel.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;

import java.util.List;

public class DistanceAdapter extends BaseQuickAdapter <String, BaseViewHolder>{
    private int posion=-1;
    public DistanceAdapter( @Nullable List<String> data) {
        super(R.layout.popup_item_oil, data);

    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
            helper.setText(R.id.oilname,item);
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
