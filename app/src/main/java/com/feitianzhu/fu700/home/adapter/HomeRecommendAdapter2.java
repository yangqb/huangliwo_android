package com.feitianzhu.fu700.home.adapter;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.home.entity.HomeEntity;
import com.feitianzhu.fu700.home.entity.ShopAndMerchants;
import com.feitianzhu.fu700.model.BaseGoodsListBean;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Locale;


/**
 * @class name：com.feitianzhu.fu700.home.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/16 0016 下午 4:46
 */
public class HomeRecommendAdapter2 extends BaseMultiItemQuickAdapter<ShopAndMerchants, BaseViewHolder> {

    public HomeRecommendAdapter2(List<ShopAndMerchants> data) {
        super(data);
        addItemType(ShopAndMerchants.TYPE_SERIES, R.layout.home_categoty_item);
        addItemType(ShopAndMerchants.TYPE_PESALE, R.layout.home_categoty_item);
    }

    @Override
    public void convert(@NonNull BaseViewHolder holder, ShopAndMerchants item) {
        switch (item.getItemType()) {
            case ShopAndMerchants.TYPE_SERIES:
                BaseGoodsListBean shopsList = item.getShopsList();
                holder.setText(R.id.tv_category, shopsList.getGoodsName());
                holder.setText(R.id.summary, shopsList.getSummary());
                holder.setText(R.id.price, "¥" + String.format(Locale.getDefault(), "%.2f", shopsList.getPrice()));
                holder.setVisible(R.id.price, true);
                holder.setVisible(R.id.address, false);
                if (shopsList.getGoodsImg() != null) {
                    Glide.with(mContext).load(shopsList.getGoodsImg()).apply(RequestOptions.errorOf(R.mipmap.a06_01tupian).placeholder(R.mipmap.a06_01tupian)).into((RoundedImageView) holder.getView(R.id.image));
                } else {
                    Glide.with(mContext).load(R.mipmap.a06_01tupian).into((RoundedImageView) holder.getView(R.id.image));
                }
                break;
            case ShopAndMerchants.TYPE_PESALE:
                HomeEntity.RecommendListBean recommendListBean = item.getRecommendListBean();
                holder.setText(R.id.tv_category, recommendListBean.merchantName);
                holder.setText(R.id.address, "");
                holder.setVisible(R.id.price, false);
                holder.setVisible(R.id.address, true);
                if (recommendListBean.merchantHeadImg != null) {
                    Glide.with(mContext).load(recommendListBean.merchantHeadImg).apply(RequestOptions.errorOf(R.mipmap.a06_01tupian).placeholder(R.mipmap.a06_01tupian)).into((RoundedImageView) holder.getView(R.id.image));
                } else {
                    Glide.with(mContext).load(R.mipmap.a06_01tupian).into((RoundedImageView) holder.getView(R.id.image));
                }
                break;
        }
    }
}
