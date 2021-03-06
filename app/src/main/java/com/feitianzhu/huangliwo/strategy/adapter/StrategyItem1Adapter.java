package com.feitianzhu.huangliwo.strategy.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.core.DateUtil;
import com.feitianzhu.huangliwo.strategy.bean.ListPageBean;

import java.util.List;

/**
 *
 */
public class StrategyItem1Adapter extends BaseQuickAdapter<ListPageBean.ListBean, BaseViewHolder> {

    public StrategyItem1Adapter(List<ListPageBean.ListBean> list) {
        super(R.layout.fragment_strategy_child1, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, ListPageBean.ListBean item) {
        helper.setText(R.id.item_number, item.getTitle());
        helper.setText(R.id.content, item.getUpdateTime());
        ImageView view = helper.getView(R.id.imageView9);
        Glide.with(mContext)
                .load(item.getImages())
                .apply(new RequestOptions()
                        .placeholder(R.color.color_CCCCCC)
                        .error(R.color.color_CCCCCC)
                        .dontAnimate())
                .into(view);

    }


}
