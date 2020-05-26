package com.feitianzhu.huangliwo.vip.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.vip.bean.VipBean;

import java.util.List;

/**
 * Created by bch on 2020/5/26
 */
public class VipShowAdapter extends BaseQuickAdapter<VipBean, BaseViewHolder> {
    public VipShowAdapter(@Nullable List<VipBean> data) {
        super(R.layout.item_vip, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, VipBean item) {
        helper.setText(R.id.title, item.getTitle());
        ImageView view = helper.getView(R.id.img);
        view.setImageDrawable(mContext.getResources().getDrawable(item.getId()));
    }
}
