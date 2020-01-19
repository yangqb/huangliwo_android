package com.feitianzhu.huangliwo.pushshop.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.ui.VerificationActivity2;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.utils.DateUtils;
import com.feitianzhu.huangliwo.view.CircleImageView;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * package name: com.feitianzhu.fu700.pushshop.adapter
 * user: yangqinbo
 * date: 2019/12/11
 * time: 14:04
 * email: 694125155@qq.com
 */
public class PushShopAdapter extends BaseQuickAdapter<MerchantsModel, BaseViewHolder> {
    public PushShopAdapter(@Nullable List<MerchantsModel> data) {
        super(R.layout.layout_push_shop, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MerchantsModel item) {
        helper.setText(R.id.merchants_Name, item.getMerchantName());
        helper.setText(R.id.tvDate, item.getCreateDate());
        Glide.with(mContext).load(item.getLogo()).apply(new RequestOptions().error(R.mipmap.b08_01touxiang).placeholder(R.mipmap.b08_01touxiang)).into((CircleImageView) helper.getView(R.id.merchantsImg));
        if (item.getStatus() == 0) {
            helper.setText(R.id.tvStatus, "审核中");
            helper.setVisible(R.id.image, false);
        } else if (item.getStatus() == 1) {
            helper.setText(R.id.tvStatus, "已通过");
            helper.setVisible(R.id.image, false);
        } else if (item.getStatus() == -1) {
            helper.setText(R.id.tvStatus, "未通过");
            helper.setVisible(R.id.image, true);
        }
    }
}
