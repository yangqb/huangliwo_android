package com.feitianzhu.huangliwo.im.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.im.bean.ConverzServiceListBean;
import com.feitianzhu.huangliwo.travel.bean.OilListBean;

import java.util.List;

public class ConverServiceAdapter extends BaseQuickAdapter<ConverzServiceListBean, BaseViewHolder> {


    public ConverServiceAdapter(@Nullable List<ConverzServiceListBean> data) {
        super(R.layout.conver_service_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ConverzServiceListBean item) {
        helper.setText(R.id.converservicename, item.getNick());
        Glide.with(mContext).load(item.getIcon())
                .apply(new RequestOptions()
                        .error(R.mipmap.g10_04weijiazai))
                .into((ImageView) helper.getView(R.id.converserviceimg));

    }
}
