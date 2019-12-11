package com.feitianzhu.fu700.shop.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.model.MultiItemComment;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * package name: com.feitianzhu.fu700.shop.adapter
 * user: yangqinbo
 * date: 2019/12/9
 * time: 10:29
 * email: 694125155@qq.com
 */
public class EditCommentAdapter extends BaseMultiItemQuickAdapter<MultiItemComment, BaseViewHolder> {
    public EditCommentAdapter(@Nullable List<MultiItemComment> data) {
        super(data);
        addItemType(1, R.layout.layout_edit_comment);
        addItemType(2, R.layout.layout_edit_comment);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultiItemComment item) {
        if (getItemViewType(helper.getAdapterPosition()) == MultiItemComment.upImg) {
            Glide.with(mContext).load(R.mipmap.g01_01shangchuan)
                    .into((RoundedImageView) helper.getView(R.id.roundImage));
            helper.setVisible(R.id.btn_cancel, false);
        } else {
            Glide.with(mContext).load(item.getPath()).apply(new RequestOptions()
                    .placeholder(R.drawable.pic_fuwutujiazaishibai)
                    .error(R.drawable.pic_fuwutujiazaishibai))
                    .into((RoundedImageView) helper.getView(R.id.roundImage));
            helper.setVisible(R.id.btn_cancel, true);
        }

        helper.addOnClickListener(R.id.btn_cancel);

    }
}
