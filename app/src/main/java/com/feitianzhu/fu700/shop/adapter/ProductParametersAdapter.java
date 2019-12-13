package com.feitianzhu.fu700.shop.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.model.ProductParameters;

import java.util.List;

/**
 * package name: com.feitianzhu.fu700.shop.adapter
 * user: yangqinbo
 * date: 2019/12/9
 * time: 17:50
 * email: 694125155@qq.com
 */
public class ProductParametersAdapter extends BaseQuickAdapter<ProductParameters.GoodslistBean.SkuValueListBean, BaseViewHolder> {
    private int selectPos = -1;

    public ProductParametersAdapter(@Nullable List<ProductParameters.GoodslistBean.SkuValueListBean> data) {
        super(R.layout.layout_product_parameters, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ProductParameters.GoodslistBean.SkuValueListBean item) {
        helper.setText(R.id.tvContent, item.getAttributeVal());
        if (selectPos == helper.getAdapterPosition()) {
            helper.getView(R.id.tvContent).setSelected(true);
        } else {
            helper.getView(R.id.tvContent).setSelected(false);
        }
    }

    public void setSelect(int selectPos) {
        this.selectPos = selectPos;
    }
}
