package com.feitianzhu.fu700.shop.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ExpandableListView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.model.ProductParameters;
import com.feitianzhu.fu700.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * package name: com.feitianzhu.fu700.shop.adapter
 * user: yangqinbo
 * date: 2019/12/9
 * time: 17:50
 * email: 694125155@qq.com
 */
public class ProductParametersAdapter extends BaseQuickAdapter<ProductParameters.GoodsSpecifications, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    private List<ProductParameters.GoodsSpecifications.SkuValueListBean> skuValueListBeanList = new ArrayList<>();
    private SpecificationAdapter mAdapter;
    private OnChildClickListener onItemClickListener;
    private int pos = -1;

    public interface OnChildClickListener {
        //成功的方法传 int 的索引
        void success(int index, int pos);
    }

    public void setOnChildPositionListener(OnChildClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public ProductParametersAdapter(List<ProductParameters.GoodsSpecifications> data) {
        super(R.layout.product_parameters_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ProductParameters.GoodsSpecifications item) {
        helper.setText(R.id.name, item.getAttributeName());
        RecyclerView recyclerView = helper.getView(R.id.recyclerView);
        mAdapter = new SpecificationAdapter(item.getSkuValueList());
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                onItemClickListener.success(position, helper.getAdapterPosition());
            }
        });
    }

    public void setSelect(int pos) {
        this.pos = pos;
    }
}
