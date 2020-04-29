package com.feitianzhu.huangliwo.pushshop.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.MultiItemComment;
import com.feitianzhu.huangliwo.pushshop.bean.MultiShopFront;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.pushshop.adapter
 * user: yangqinbo
 * date: 2020/4/29
 * time: 11:06
 * email: 694125155@qq.com
 */
public class ShopFrontAdapter extends BaseMultiItemQuickAdapter<MultiShopFront, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ShopFrontAdapter(List<MultiShopFront> data) {
        super(data);
        addItemType(1, R.layout.layout_shop_front);
        addItemType(2, R.layout.layout_shop_front);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultiShopFront item) {
        if (getItemViewType(helper.getAdapterPosition()) == MultiItemComment.upImg) {
            Glide.with(mContext).load(R.mipmap.g01_01shangchuan)
                    .into((RoundedImageView) helper.getView(R.id.roundImage));
            helper.setVisible(R.id.btn_cancel, false);
        } else {
            Glide.with(mContext).load(item.getPath()).apply(new RequestOptions()
                    .dontAnimate()
                    .placeholder(R.mipmap.g10_04weijiazai)
                    .error(R.mipmap.g10_04weijiazai))
                    .into((RoundedImageView) helper.getView(R.id.roundImage));
            helper.setVisible(R.id.btn_cancel, true);
        }

        helper.addOnClickListener(R.id.btn_cancel);
    }
}
