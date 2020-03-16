package com.feitianzhu.huangliwo.message;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.utils.GlideUtils;

import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.message
 * user: yangqinbo
 * date: 2020/3/7
 * time: 18:19
 * email: 694125155@qq.com
 */
public class DiscoverAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public DiscoverAdapter(@Nullable List<String> data) {
        super(R.layout.item_discover, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        ImageView imageView = helper.getView(R.id.imageView);
        Glide.with(mContext).load(item).into(GlideUtils.getImageView3((Activity) mContext, item, imageView));
    }
}
