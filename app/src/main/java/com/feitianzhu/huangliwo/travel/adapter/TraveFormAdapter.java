package com.feitianzhu.huangliwo.travel.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;

import java.util.List;

/**
 * Created by bch on 2020/5/19
 */
public class TraveFormAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public TraveFormAdapter(@Nullable List<String> data) {
        super(R.layout.item_trave_form, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        helper.setText(R.id.name, item);
    }

}
