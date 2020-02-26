package com.feitianzhu.huangliwo.plane;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.MainActivity;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.shop.NewYearShoppingActivity;
import com.feitianzhu.huangliwo.view.CustomCancelChangePopView;
import com.feitianzhu.huangliwo.view.CustomLuggageBuyTicketNoticeView;
import com.feitianzhu.huangliwo.view.CustomNerYearPopView;
import com.feitianzhu.huangliwo.view.CustomPlaneInfoView;
import com.feitianzhu.huangliwo.view.CustomTicketPriceDetailView;
import com.lxj.xpopup.XPopup;

import butterknife.BindView;
import butterknife.OnClick;

public class EditPlaneReserveActivity extends BaseActivity {
    public static final String PLANE_TYPE = "plane_type";
    private int type;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.plane_title)
    LinearLayout planeTitle;
    @BindView(R.id.startCity)
    TextView startCity;
    @BindView(R.id.endCity)
    TextView endCity;
    @BindView(R.id.center_img)
    ImageView centerImg;
    @BindView(R.id.ll_goPlane)
    LinearLayout llGoPlane;
    @BindView(R.id.come_back)
    TextView comeBack;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_plane_reserve;
    }

    @Override
    protected void initView() {
        type = getIntent().getIntExtra(PLANE_TYPE, 1);
        planeTitle.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.GONE);
        startCity.setText("北京");
        endCity.setText("上海");
        if (type == 2) {
            centerImg.setBackgroundResource(R.mipmap.k01_13wangfan);
            llGoPlane.setVisibility(View.VISIBLE);
            comeBack.setVisibility(View.VISIBLE);
        } else {
            centerImg.setBackgroundResource(R.mipmap.k01_12quwang);
            llGoPlane.setVisibility(View.GONE);
            comeBack.setVisibility(View.GONE);
        }


    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.cancel_change, R.id.rl_plane_info, R.id.luggage_buyTicket_notice, R.id.ticketPrice_detail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.cancel_change:
                new XPopup.Builder(EditPlaneReserveActivity.this)
                        .enableDrag(false)
                        .asCustom(new CustomCancelChangePopView(this
                        ).setType(type)).show();
                break;
            case R.id.rl_plane_info:
                new XPopup.Builder(EditPlaneReserveActivity.this)
                        .enableDrag(false)
                        .asCustom(new CustomPlaneInfoView(this
                        ).setType(type)).show();
                break;
            case R.id.luggage_buyTicket_notice:
                new XPopup.Builder(EditPlaneReserveActivity.this)
                        .enableDrag(false)
                        .asCustom(new CustomLuggageBuyTicketNoticeView(this
                        ).setType(type)).show();
                break;
            case R.id.ticketPrice_detail:
                new XPopup.Builder(EditPlaneReserveActivity.this)
                        .enableDrag(false)
                        .asCustom(new CustomTicketPriceDetailView(this
                        ).setType(type)).show();
                break;
        }
    }
}
