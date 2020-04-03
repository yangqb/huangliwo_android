package com.feitianzhu.huangliwo.home.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.home.entity.IndicatorEntity;

import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.home.adapter
 * user: yangqinbo
 * date: 2020/4/1
 * time: 17:19
 * email: 694125155@qq.com
 */
public class IndicatorAdapter extends BaseQuickAdapter<IndicatorEntity, BaseViewHolder> {
    public IndicatorAdapter(@Nullable List<IndicatorEntity> data) {
        super(R.layout.layout_indicator, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, IndicatorEntity item) {
        if (item.isSelect) {
            helper.setBackgroundRes(R.id.indicator, R.color.bg_yellow);
        } else {
            helper.setBackgroundRes(R.id.indicator, R.color.color_DDDDDD);
        }
    }
}
