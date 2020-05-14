package com.feitianzhu.huangliwo.pushshop.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.pushshop.bean.MultiMerchantsModel;
import com.feitianzhu.huangliwo.view.CircleImageView;

import java.util.List;

/**
 * package name: com.feitianzhu.fu700.pushshop.adapter
 * user: yangqinbo
 * date: 2019/12/11
 * time: 14:04
 * email: 694125155@qq.com
 */
public class PushShopAdapter extends BaseMultiItemQuickAdapter<MultiMerchantsModel, BaseViewHolder> {
    public PushShopAdapter(@Nullable List<MultiMerchantsModel> data) {
        super(data);
        addItemType(MultiMerchantsModel.EXAMINE_TYPE, R.layout.layout_push_shop_examine);
        addItemType(MultiMerchantsModel.PASSED_TYPE, R.layout.layout_push_shop_pass);
        addItemType(MultiMerchantsModel.UNPASSED_TYPE, R.layout.layout_push_shop_unpass);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultiMerchantsModel item) {
        helper.addOnClickListener(R.id.share);
        if (helper.getItemViewType() == MultiMerchantsModel.EXAMINE_TYPE) {
            helper.setText(R.id.merchants_Name, item.merchants.getMerchantName());
            helper.setText(R.id.tvDate, item.merchants.getCreateDate());
            Glide.with(mContext).load(item.merchants.getLogo()).apply(new RequestOptions().error(R.mipmap.g10_04weijiazai).placeholder(R.mipmap.g10_04weijiazai)).into((CircleImageView) helper.getView(R.id.merchantsImg));
            helper.setText(R.id.tvStatus, "审核中");
        } else if (helper.getItemViewType() == MultiMerchantsModel.PASSED_TYPE) {
            helper.setText(R.id.merchants_Name, item.merchants.getMerchantName());
            helper.setText(R.id.tvDate, item.merchants.getCreateDate());
            Glide.with(mContext).load(item.merchants.getLogo()).apply(new RequestOptions().error(R.mipmap.g10_04weijiazai).placeholder(R.mipmap.g10_04weijiazai)).into((CircleImageView) helper.getView(R.id.merchantsImg));
            helper.setText(R.id.tvStatus, "已通过");
        } else {
            helper.setText(R.id.merchants_Name, item.merchants.getMerchantName());
            helper.setText(R.id.tvDate, item.merchants.getCreateDate());
            Glide.with(mContext).load(item.merchants.getLogo()).apply(new RequestOptions().error(R.mipmap.g10_04weijiazai).placeholder(R.mipmap.g10_04weijiazai)).into((CircleImageView) helper.getView(R.id.merchantsImg));
            helper.setText(R.id.tvStatus, "未通过");
        }
    }
}
