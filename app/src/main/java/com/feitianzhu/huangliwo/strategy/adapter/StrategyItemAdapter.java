package com.feitianzhu.huangliwo.strategy.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.strategy.bean.ListPageBean;

import java.util.List;

/**
 */
public class StrategyItemAdapter extends BaseQuickAdapter<ListPageBean.ListBean, BaseViewHolder> {

    public StrategyItemAdapter(List<ListPageBean.ListBean> list) {
        super(R.layout.fragment_strategy_child, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, ListPageBean.ListBean item) {
        helper.setText(R.id.item_number, item.getTitle());
        helper.setText(R.id.content, item.getTitle());

        ImageView view = helper.getView(R.id.imageView9);
        Glide.with(mContext)
                .load(item.getImages())
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.g10_04weijiazai)
                        .error(R.mipmap.g10_04weijiazai)
                        .dontAnimate())
                .into(view);

    }


}
