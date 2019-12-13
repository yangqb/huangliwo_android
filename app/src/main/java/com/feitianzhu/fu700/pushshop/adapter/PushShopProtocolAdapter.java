package com.feitianzhu.fu700.pushshop.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.utils.GlideUtils;

import java.util.List;

/**
 * package name: com.feitianzhu.fu700.pushshop.adapter
 * user: yangqinbo
 * date: 2019/12/11
 * time: 20:08
 * email: 694125155@qq.com
 */
public class PushShopProtocolAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    public PushShopProtocolAdapter(@Nullable List<Integer> data) {
        super(R.layout.layout_push_shop_protocol, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Integer item) {
        Glide.with(mContext).load(item)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.pic_fuwutujiazaishibai)
                        .error(R.drawable.pic_fuwutujiazaishibai)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL))
                .into((ImageView) helper.getView(R.id.imageView));
    }
}
