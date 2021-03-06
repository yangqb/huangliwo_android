package com.feitianzhu.huangliwo.home.adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.ShopClassify;
import com.feitianzhu.huangliwo.view.CircleImageView;

import java.util.List;


/**
 * @class name：com.feitianzhu.fu700.home.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/16 0016 下午 5:56
 */
public class HAdapter extends BaseQuickAdapter<ShopClassify.GGoodsClsListBean, BaseViewHolder> {

    public HAdapter(List<ShopClassify.GGoodsClsListBean> serviceRecommendList) {
        super(R.layout.home_horizontal_item, serviceRecommendList);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopClassify.GGoodsClsListBean item) {
        helper.setText(R.id.name, item.getClsName());
        Glide.with(mContext).load(item.getClsImg()).apply(new RequestOptions().dontAnimate()).into((CircleImageView) helper.getView(R.id.image));
    }
}
