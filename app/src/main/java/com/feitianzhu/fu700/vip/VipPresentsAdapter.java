package com.feitianzhu.fu700.vip;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.model.PresentsModel;

import java.util.List;

/**
 * package name: com.feitianzhu.fu700.vip
 * user: yangqinbo
 * date: 2019/12/30
 * time: 14:35
 * email: 694125155@qq.com
 */
public class VipPresentsAdapter extends BaseQuickAdapter<PresentsModel.ShopGiftListBean, BaseViewHolder> {
    public VipPresentsAdapter(@Nullable List<PresentsModel.ShopGiftListBean> data) {
        super(R.layout.layout_vip_presents, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PresentsModel.ShopGiftListBean item) {
        Glide.with(mContext).load(item.getGiftImg()).apply(new RequestOptions().error(R.mipmap.g10_04weijiazai).placeholder(R.mipmap.g10_04weijiazai)).into((ImageView) helper.getView(R.id.imageView));
        helper.setText(R.id.tvTitle, item.getGiftTitle());
    }
}
