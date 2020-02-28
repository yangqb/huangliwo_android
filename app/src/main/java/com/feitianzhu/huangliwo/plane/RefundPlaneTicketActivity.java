package com.feitianzhu.huangliwo.plane;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.view.CustomPassengerNameView;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RefundPlaneTicketActivity extends BaseActivity {
    private RefundPlaneTicketPassengerAdapter mAdapter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_refund_plane_ticket;
    }

    @Override
    protected void initView() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add(i);
        }
        mAdapter = new RefundPlaneTicketPassengerAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.refund_reason, R.id.invoiceType, R.id.identification_num_explain})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.refund_reason:
                String[] strings2 = new String[]{"我要改变行程", "填错信息", "航班延误或取消"};
                new XPopup.Builder(this)
                        .asCustom(new CustomRefundView(RefundPlaneTicketActivity.this)
                                .setData(Arrays.asList(strings2))
                                .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                    }
                                }))
                        .show();
                break;
            case R.id.invoiceType:
                String[] strings1 = new String[]{"企业", "个人", "政府机关行政单位"};
                new XPopup.Builder(this)
                        .asCustom(new CustomRefundView(RefundPlaneTicketActivity.this)
                                .setData(Arrays.asList(strings1))
                                .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                    }
                                }))
                        .show();
                break;
            case R.id.identification_num_explain:
                String content = "纳税人识别号是企业税务登记证上唯一识别企业的税号，由15/18或20位数码组成。根据国家税务局规定，自2017年7月1日起，开具增值税普通发票必须有纳税人识别号或统一社会信用代码，否则该发票为不符合规定的发票，不得作为税收凭证。纳税人识别号可登录纳税人信息查询网www.yibannashuiren.com 查询，或向公司财务人员咨询。";

                new XPopup.Builder(RefundPlaneTicketActivity.this)
                        .enableDrag(false)
                        .asCustom(new CustomPassengerNameView(this
                        ).setContent(content)).show();
                break;

        }
    }
}
