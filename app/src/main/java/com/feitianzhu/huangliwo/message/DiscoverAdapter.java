package com.feitianzhu.huangliwo.message;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.utils.GlideUtils;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.message
 * user: yangqinbo
 * date: 2020/3/7
 * time: 18:19
 * email: 694125155@qq.com
 */
public class DiscoverAdapter extends BaseQuickAdapter<BaseGoodsListBean, BaseViewHolder> {
    public DiscoverAdapter(@Nullable List<BaseGoodsListBean> data) {
        super(R.layout.item_discover, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BaseGoodsListBean item) {
        RoundedImageView imageView = helper.getView(R.id.imageView);
        GlideUtils.getImageView3((Activity) mContext, item.getGoodsImg(), imageView);
        //Glide.with(mContext).load(item.getGoodsImg()).apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into();
        helper.setText(R.id.goodsName, item.getGoodsName());
    }
}
