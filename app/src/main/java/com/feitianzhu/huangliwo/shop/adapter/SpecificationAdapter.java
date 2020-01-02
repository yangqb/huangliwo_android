package com.feitianzhu.huangliwo.shop.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.ProductParameters;

import java.util.List;

/**
 * package name: com.feitianzhu.fu700.shop.adapter
 * user: yangqinbo
 * date: 2019/12/18
 * time: 17:25
 * email: 694125155@qq.com
 */
public class SpecificationAdapter extends BaseQuickAdapter<ProductParameters.GoodsSpecifications.SkuValueListBean, BaseViewHolder> {
    public SpecificationAdapter(@Nullable List<ProductParameters.GoodsSpecifications.SkuValueListBean> data) {
        super(R.layout.layout_product_parameters, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ProductParameters.GoodsSpecifications.SkuValueListBean item) {
        helper.setText(R.id.tvContent, item.getAttributeVal());
       // helper.addOnClickListener(R.id.tvContent);
        if (item.isSelect()) {
            helper.getView(R.id.tvContent).setSelected(true);
        } else {
            helper.getView(R.id.tvContent).setSelected(false);
        }
    }
}
