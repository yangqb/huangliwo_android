package com.feitianzhu.huangliwo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.plane.PlaneCancelChangeAdapter;
import com.lxj.xpopup.core.CenterPopupView;

import java.util.ArrayList;
import java.util.List;

public class CustomCancelChangePopView extends CenterPopupView {
    private PlaneCancelChangeAdapter mAdapter;
    private Context mContext;
    private int type;

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_cancel_change;
    }

    public CustomCancelChangePopView(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public CustomCancelChangePopView setType(int type) {
        this.type = type;
        return this;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            list.add(i);
        }
        if (type == 2) {
            findViewById(R.id.goTitle).setVisibility(VISIBLE);
            findViewById(R.id.llComeBack).setVisibility(VISIBLE);
        } else {
            findViewById(R.id.goTitle).setVisibility(GONE);
            findViewById(R.id.llComeBack).setVisibility(GONE);
        }
        mAdapter = new PlaneCancelChangeAdapter(list);
        RecyclerView recyclerView1 = findViewById(R.id.recyclerView1);
        RecyclerView recyclerView2 = findViewById(R.id.recyclerView2);
        RecyclerView recyclerView3 = findViewById(R.id.recyclerView3);
        RecyclerView recyclerView4 = findViewById(R.id.recyclerView4);
        RecyclerView recyclerView5 = findViewById(R.id.recyclerView5);
        RecyclerView recyclerView6 = findViewById(R.id.recyclerView6);
        RecyclerView recyclerView7 = findViewById(R.id.recyclerView7);
        RecyclerView recyclerView8 = findViewById(R.id.recyclerView8);
        recyclerView1.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView2.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView3.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView4.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView5.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView6.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView7.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView8.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView1.setAdapter(mAdapter);
        recyclerView2.setAdapter(mAdapter);
        recyclerView3.setAdapter(mAdapter);
        recyclerView4.setAdapter(mAdapter);
        recyclerView5.setAdapter(mAdapter);
        recyclerView6.setAdapter(mAdapter);
        recyclerView7.setAdapter(mAdapter);
        recyclerView8.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }
}
