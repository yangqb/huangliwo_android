package com.feitianzhu.huangliwo.shop.adapter;

import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * @class name：com.feitianzhu.fu700.shop.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/21 0021 下午 7:51
 */
public class CommentImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public CommentImgAdapter(List<String> list) {
        super(R.layout.comment_img_item, list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        Glide.with(mContext).load(item)
                .apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai)).into((RoundedImageView) helper.getView(R.id.imageView));
    }


}
