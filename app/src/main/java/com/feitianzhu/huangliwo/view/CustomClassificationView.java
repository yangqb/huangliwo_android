package com.feitianzhu.huangliwo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.lxj.xpopup.core.BottomPopupView;

import java.util.ArrayList;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.view
 * user: yangqinbo
 * date: 2020/4/17
 * time: 12:25
 * email: 694125155@qq.com
 */
public class CustomClassificationView extends BottomPopupView {
    private CustomRefundViewAdapter adapter;
    private RecyclerView recyclerView;
    private Context mContext;
    private List<String> data = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private CustomClassificationView.OnItemClickListener mOnItemClickListener;

    public CustomClassificationView setOnItemClickListener(CustomClassificationView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        return this;
    }

    public CustomClassificationView(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public CustomClassificationView setData(List<String> data) {
        this.data = data;
        return this;
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_classification_dialog;
    }

    // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new CustomRefundViewAdapter(data);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listener();
    }

    public void listener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                    dismiss();
                }
            }
        });

    }
}
