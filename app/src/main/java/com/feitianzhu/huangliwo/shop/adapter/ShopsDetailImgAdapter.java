package com.feitianzhu.huangliwo.shop.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.utils.GlideUtils;

import java.io.File;
import java.util.List;

public class ShopsDetailImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ShopsDetailImgAdapter(@Nullable List<String> data) {
        super(R.layout.layout_goods_detail_img, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        SubsamplingScaleImageView imageView = helper.getView(R.id.imageView);
        Glide.with(mContext).load(item).apply(new RequestOptions().placeholder(R.color.color_f7f7f7).dontAnimate()).downloadOnly(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File resource, Transition<? super File> transition) {
                Uri uri = Uri.fromFile(resource);
                imageView.setImage(ImageSource.uri(uri));
                imageView.setZoomEnabled(false);
                imageView.setPanEnabled(false);
            }
        });
    }
}
