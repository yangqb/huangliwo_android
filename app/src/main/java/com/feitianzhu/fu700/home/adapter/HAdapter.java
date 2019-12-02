package com.feitianzhu.fu700.home.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.home.entity.HomeEntity;

import java.util.List;


/**
 * @class name：com.feitianzhu.fu700.home.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/16 0016 下午 5:56
 */
public class HAdapter extends BaseQuickAdapter<HomeEntity.ServiceRecommendListBean, BaseViewHolder> {

    public HAdapter(List<HomeEntity.ServiceRecommendListBean> serviceRecommendList) {
        super(R.layout.home_horizontal_item, serviceRecommendList);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeEntity.ServiceRecommendListBean item) {
        helper.setText(R.id.name, item.serviceName);
    }
}
