package com.feitianzhu.huangliwo.plane;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.view.CustomCancelChangePopView;
import com.lxj.xpopup.XPopup;

import butterknife.BindView;
import butterknife.OnClick;

public class PlaneOrderDetailActivity extends BaseActivity {
    public static final String ORDER_TYPE = "order_type";
    public static final String PLANE_TYPE = "plane_type";

    private int orderType;
    private int planeType;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.ll_go_bg)
    LinearLayout ll_go_bg;
    @BindView(R.id.tv_go_tag)
    TextView tv_go_tag;
    @BindView(R.id.ll_go_title)
    LinearLayout ll_go_title;
    @BindView(R.id.ll_come_detail)
    LinearLayout ll_come_detail;
    @BindView(R.id.rl_refund_change)
    RelativeLayout rl_refund_change;
    @BindView(R.id.rl_bottom)
    RelativeLayout rl_bottom;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plane_order_detail;
    }

    @Override
    protected void initView() {
        planeType = getIntent().getIntExtra(PLANE_TYPE, 0);
        orderType = getIntent().getIntExtra(ORDER_TYPE, 0);
        if (planeType == 1) {
            ll_go_bg.setVisibility(View.GONE);
            tv_go_tag.setVisibility(View.GONE);
            ll_go_title.setVisibility(View.VISIBLE);
            ll_come_detail.setVisibility(View.GONE);
            if (orderType == 1) {
                rl_refund_change.setVisibility(View.GONE);
                rl_bottom.setVisibility(View.VISIBLE);
            } else {
                rl_refund_change.setVisibility(View.VISIBLE);
                rl_bottom.setVisibility(View.GONE);
            }
        } else {
            ll_go_bg.setVisibility(View.VISIBLE);
            tv_go_tag.setVisibility(View.VISIBLE);
            ll_go_title.setVisibility(View.GONE);
            ll_come_detail.setVisibility(View.VISIBLE);
            if (orderType == 1) {
                rl_refund_change.setVisibility(View.GONE);
                rl_bottom.setVisibility(View.VISIBLE);
            } else {
                rl_refund_change.setVisibility(View.VISIBLE);
                rl_bottom.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.btn_reimbursement, R.id.btn_refund, R.id.btn_change, R.id.left_button, R.id.rl_luggage_change_notice})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.btn_reimbursement:
                intent = new Intent(PlaneOrderDetailActivity.this, PlaneReimbursementActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_refund:
                intent = new Intent(PlaneOrderDetailActivity.this, RefundPlaneTicketActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_change:
                intent = new Intent(PlaneOrderDetailActivity.this, PlaneChangeActivity.class);
                intent.putExtra(PlaneChangeActivity.PLANE_TYPE, planeType);
                startActivity(intent);
                break;
            case R.id.rl_luggage_change_notice:
                new XPopup.Builder(PlaneOrderDetailActivity.this)
                        .enableDrag(false)
                        .asCustom(new CustomCancelChangePopView(PlaneOrderDetailActivity.this
                        ).setType(planeType).setLuggage(true)).show();
                break;
        }
    }
}
