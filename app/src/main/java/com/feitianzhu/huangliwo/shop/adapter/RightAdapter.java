package com.feitianzhu.huangliwo.shop.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.model.MultipleItem;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.itheima.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Locale;

/**
 * @class name：com.feitianzhu.fu700.shop.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/20 0020 下午 2:53
 */
public class RightAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {
    public RightAdapter(List<MultipleItem> data) {
        super(data);
        addItemType(MultipleItem.MERCHANTS, R.layout.shop_right_item2);
        addItemType(MultipleItem.GOODS, R.layout.shop_right_item2);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        MineInfoModel userInfo = UserInfoUtils.getUserInfo(mContext);
        switch (helper.getItemViewType()) {
            case MultipleItem.MERCHANTS:
                helper.setText(R.id.text, item.getMerchantsModel().getAreaName());

                helper.addOnClickListener(R.id.ll_rebate);
                Glide.with(mContext).load(item.getMerchantsModel().getLogo())
                        .apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai).dontAnimate()).into((RoundedImageView) helper.getView(R.id.image));

                break;
            case MultipleItem.GOODS:
                helper.setText(R.id.text, item.getGoodsListBean().getGoodsName());

                helper.addOnClickListener(R.id.ll_rebate);
                Glide.with(mContext).load(item.getGoodsListBean().getGoodsImg())
                        .apply(new RequestOptions().placeholder(R.mipmap.g10_04weijiazai).error(R.mipmap.g10_04weijiazai).dontAnimate()).into((RoundedImageView) helper.getView(R.id.image));
                break;
        }
    }

}
