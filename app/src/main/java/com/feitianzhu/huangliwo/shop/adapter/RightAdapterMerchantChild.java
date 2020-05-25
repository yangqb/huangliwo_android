package com.feitianzhu.huangliwo.shop.adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * @class name：com.feitianzhu.fu700.shop.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/20 0020 下午 2:53
 */
public class RightAdapterMerchantChild extends BaseQuickAdapter<MerchantsModel, BaseViewHolder> {
    public RightAdapterMerchantChild(List<MerchantsModel> data) {
        super(R.layout.shop_right_item3, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MerchantsModel item) {
        helper.setText(R.id.text1, item.getMerchantName());
        Glide.with(mContext).load(item.getLogo())
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.g10_04weijiazai)
                        .error(R.mipmap.g10_04weijiazai)
                        .dontAnimate())
                .into((RoundedImageView) helper.getView(R.id.image));
    }

}
