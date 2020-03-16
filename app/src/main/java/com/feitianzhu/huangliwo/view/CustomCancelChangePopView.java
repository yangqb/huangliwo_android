package com.feitianzhu.huangliwo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.CustomTgqChangeModel;
import com.feitianzhu.huangliwo.model.RefundChangeInfo;
import com.feitianzhu.huangliwo.plane.PlaneCancelChangeAdapter;
import com.lxj.xpopup.core.CenterPopupView;

import java.util.ArrayList;
import java.util.List;

public class CustomCancelChangePopView extends CenterPopupView {
    private RefundChangeInfo refundChangeInfo;
    private PlaneCancelChangeAdapter mAdapter1;
    private PlaneCancelChangeAdapter mAdapter2;
    private PlaneCancelChangeAdapter mAdapter3;
    private PlaneCancelChangeAdapter mAdapter4;
    private PlaneCancelChangeAdapter mAdapter5;
    private PlaneCancelChangeAdapter mAdapter6;
    private PlaneCancelChangeAdapter mAdapter7;
    private PlaneCancelChangeAdapter mAdapter8;
    private Context mContext;
    private int type;
    private boolean isLuggage;

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

    public CustomCancelChangePopView setData(RefundChangeInfo refundChangeInfo) {
        this.refundChangeInfo = refundChangeInfo;
        return this;
    }

    public CustomCancelChangePopView setLuggage(boolean isLuggage) {
        this.isLuggage = isLuggage;
        return this;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        LinearLayout luggage_notice = findViewById(R.id.luggage_notice);
        if (isLuggage) {
            luggage_notice.setVisibility(VISIBLE);
        } else {
            luggage_notice.setVisibility(GONE);
        }
        if (type == 0 || type == 1) {
            findViewById(R.id.goTitle).setVisibility(GONE);
            findViewById(R.id.llComeBack).setVisibility(GONE);
        } else {
            findViewById(R.id.goTitle).setVisibility(VISIBLE);
            findViewById(R.id.llComeBack).setVisibility(VISIBLE);
        }

        TextView go_signText = findViewById(R.id.go_signText);
        go_signText.setText(refundChangeInfo.signText);

        List<CustomTgqChangeModel> list1 = new ArrayList<>();
        List<CustomTgqChangeModel> list2 = new ArrayList<>();
        List<CustomTgqChangeModel> list3 = new ArrayList<>();
        List<CustomTgqChangeModel> list4 = new ArrayList<>();
        List<CustomTgqChangeModel> list5 = new ArrayList<>();
        List<CustomTgqChangeModel> list6 = new ArrayList<>();
        List<CustomTgqChangeModel> list7 = new ArrayList<>();
        List<CustomTgqChangeModel> list8 = new ArrayList<>();
        for (int i = 0; i < refundChangeInfo.tgqPointCharges.size(); i++) {
            CustomTgqChangeModel changeModel1 = new CustomTgqChangeModel();
            CustomTgqChangeModel changeModel2 = new CustomTgqChangeModel();
            changeModel1.timeText = refundChangeInfo.tgqPointCharges.get(i).timeText;
            changeModel1.amount = refundChangeInfo.tgqPointCharges.get(i).returnFee;
            changeModel2.timeText = refundChangeInfo.tgqPointCharges.get(i).timeText;
            changeModel2.amount = refundChangeInfo.tgqPointCharges.get(i).changeFee;
            list1.add(changeModel1);
            list2.add(changeModel2);
        }

        mAdapter1 = new PlaneCancelChangeAdapter(list1);
        mAdapter2 = new PlaneCancelChangeAdapter(list2);
        mAdapter3 = new PlaneCancelChangeAdapter(list3);
        mAdapter4 = new PlaneCancelChangeAdapter(list4);
        mAdapter5 = new PlaneCancelChangeAdapter(list5);
        mAdapter6 = new PlaneCancelChangeAdapter(list6);
        mAdapter7 = new PlaneCancelChangeAdapter(list7);
        mAdapter8 = new PlaneCancelChangeAdapter(list8);
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
        recyclerView1.setAdapter(mAdapter1);
        recyclerView2.setAdapter(mAdapter2);
        recyclerView3.setAdapter(mAdapter3);
        recyclerView4.setAdapter(mAdapter4);
        recyclerView5.setAdapter(mAdapter5);
        recyclerView6.setAdapter(mAdapter6);
        recyclerView7.setAdapter(mAdapter7);
        recyclerView8.setAdapter(mAdapter8);
        mAdapter1.notifyDataSetChanged();
        mAdapter2.notifyDataSetChanged();
        mAdapter3.notifyDataSetChanged();
        mAdapter4.notifyDataSetChanged();
        mAdapter5.notifyDataSetChanged();
        mAdapter6.notifyDataSetChanged();
        mAdapter7.notifyDataSetChanged();
        mAdapter8.notifyDataSetChanged();
    }
}
