package com.feitianzhu.fu700.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.model.ProductParameters;
import com.feitianzhu.fu700.shop.adapter.ProductParametersAdapter;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.lxj.xpopup.core.BottomPopupView;

import java.util.List;

/**
 * package name: com.feitianzhu.fu700.view
 * user: yangqinbo
 * date: 2019/12/17
 * time: 19:40
 * email: 694125155@qq.com
 */
public class CustomSpecificationView extends BottomPopupView {
    private Context context;
    private RelativeLayout btn_cancel;
    private TextView btn_ok;
    private RecyclerView recyclerView;
    private ProductParametersAdapter mAdapter;
    private List<ProductParameters.GoodslistBean.SkuValueListBean> data;
    private int mPosition = -1;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private CustomSpecificationView.OnItemClickListener mOnItemClickListener;

    public CustomSpecificationView setOnItemClickListener(CustomSpecificationView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        return this;
    }

    public CustomSpecificationView(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_view_specification;
    }

    // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
    @Override
    protected void onCreate() {
        super.onCreate();
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_ok = findViewById(R.id.btn_ok);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        mAdapter = new ProductParametersAdapter(data);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        listener();
    }

    public void listener() {
        btn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPosition == -1) {
                    ToastUtils.showShortToast("请选择规格");
                } else {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mPosition);
                        dismiss();
                    }
                }
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mPosition = position;
                mAdapter.setSelect(position);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public CustomSpecificationView setData(List<ProductParameters.GoodslistBean.SkuValueListBean> data) {
        this.data = data;
        return this;
    }
}
