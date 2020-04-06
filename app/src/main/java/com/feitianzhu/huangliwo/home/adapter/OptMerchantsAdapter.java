package com.feitianzhu.huangliwo.home.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;

import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.home.adapter
 * user: yangqinbo
 * date: 2020/4/1
 * time: 18:56
 * email: 694125155@qq.com
 */
public class OptMerchantsAdapter extends BaseQuickAdapter<MerchantsModel, BaseViewHolder> {
    public OptMerchantsAdapter(@Nullable List<MerchantsModel> data) {
        super(R.layout.layout_opt_merchants, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MerchantsModel item) {
        MineInfoModel userInfo = UserInfoUtils.getUserInfo(mContext);
        Glide.with(mContext).load(item.getLogo()).apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).placeholder(R.mipmap.g10_04weijiazai)).into((ImageView) helper.getView(R.id.merchants_img));
        helper.setText(R.id.merchants_Name, item.getMerchantName());
        String discount = String.valueOf((100 - item.getDiscount() * 100));
        helper.setText(R.id.tv_rebate, "返" + MathUtils.subZero(discount) + "%");
        helper.setText(R.id.vip_rebate, "返" + MathUtils.subZero(discount) + "%");
        if (userInfo.getAccountType() != 0) {
            helper.setGone(R.id.ll_rebate, false);
            helper.setGone(R.id.vip_rebate, true);
        } else {
            helper.setGone(R.id.ll_rebate, true);
            helper.setGone(R.id.vip_rebate, false);
        }
        helper.addOnClickListener(R.id.ll_rebate);
    }
}
