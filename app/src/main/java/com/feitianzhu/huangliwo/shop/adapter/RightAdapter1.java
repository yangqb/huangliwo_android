package com.feitianzhu.huangliwo.shop.adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.MultipleItem;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * @class name：com.feitianzhu.fu700.shop.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/20 0020 下午 2:53
 */
public class RightAdapter1 extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {
    public RightAdapter1(List<MultipleItem> data) {
        super(data);
        addItemType(MultipleItem.MERCHANTS, R.layout.shop_right_item3);
        addItemType(MultipleItem.GOODS, R.layout.shop_right_item3);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {

        switch (helper.getItemViewType()) {
            case MultipleItem.MERCHANTS:
                helper.setText(R.id.text1, item.getMerchantsModel().getMerchantName());
                Glide.with(mContext).load(item.getMerchantsModel().getLogo())
                        .apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai)
                                .error(R.mipmap.g10_04weijiazai)
                                .dontAnimate()).into((RoundedImageView) helper.getView(R.id.image));
                break;
            case MultipleItem.GOODS:

                helper.setText(R.id.text1, item.getGoodsListBean().getGoodsName());
                Glide.with(mContext).load(item.getGoodsListBean().getGoodsImg())
                        .apply(new RequestOptions()
                                .placeholder(R.mipmap.g10_04weijiazai)
                                .error(R.mipmap.g10_04weijiazai)
                                .dontAnimate())
                        .into((RoundedImageView) helper.getView(R.id.image));
                break;
        }
    }

}
