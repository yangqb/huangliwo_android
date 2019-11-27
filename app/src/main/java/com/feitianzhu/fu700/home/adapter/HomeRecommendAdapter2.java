package com.feitianzhu.fu700.home.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.home.entity.HomeEntity.RecommendListBean;

import java.util.List;



/**
 * @class name：com.feitianzhu.fu700.home.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/16 0016 下午 4:46
 */
public class HomeRecommendAdapter2 extends BaseQuickAdapter<RecommendListBean, BaseViewHolder> {

    public HomeRecommendAdapter2(List<RecommendListBean> data) {
        super(R.layout.home_categoty_item, data);
    }

    @Override
    public void convert(BaseViewHolder holder, RecommendListBean item) {
        holder.setText(R.id.tv_category, item.merchantName);
        //Glide.with(mContext).load(item.merchantHeadImg).bitmapTransform(new CropCircleTransformation(mContext)).placeholder(R.mipmap.pic_fuwutujiazaishibai).into((ImageView) holder.getView(R.id.iv_category));
    }
}
