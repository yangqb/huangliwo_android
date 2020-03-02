package com.feitianzhu.huangliwo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.plane.PlaneProtocolAdapter;
import com.lxj.xpopup.core.CenterPopupView;

import java.util.List;

public class CustomPlaneProtocolView extends CenterPopupView {
    private Context context;
    private String title;
    private List<Integer> list;

    public CustomPlaneProtocolView(@NonNull Context context) {
        super(context);
        this.context = context;
    }


    public CustomPlaneProtocolView setTitle(String title) {
        this.title = title;
        return this;
    }

    public CustomPlaneProtocolView setData(List<Integer> list) {
        this.list = list;
        return this;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_plane_protocol;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        TextView titleName = findViewById(R.id.titleName);
        titleName.setText(title);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        PlaneProtocolAdapter mAdapter = new PlaneProtocolAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
