package com.feitianzhu.fu700.shop.adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.model.MultipleItem;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * @class name：com.feitianzhu.fu700.shop.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/20 0020 下午 2:53
 */
public class RightAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {

    public RightAdapter(List<MultipleItem> data) {
        super(data);
        addItemType(MultipleItem.TEXT, R.layout.shop_right_item);
        addItemType(MultipleItem.IMG, R.layout.shop_right_item2);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        switch (helper.getItemViewType()) {
            case MultipleItem.TEXT:
                // helper.setImageUrl(R.id.tv, item.getContent());
                break;
            case MultipleItem.IMG:
                helper.setText(R.id.tv_category, item.getGoodsListBean().getGoodsName());
                helper.setText(R.id.price, "¥" + item.getGoodsListBean().getPrice());
                helper.setText(R.id.tvContent, item.getGoodsListBean().getSummary());
                Glide.with(mContext).load(item.getGoodsListBean().getGoodsImg())
                        .apply(new RequestOptions().placeholder(R.drawable.pic_fuwutujiazaishibai)).into((RoundedImageView) helper.getView(R.id.image));
                break;
        }
    }
}
