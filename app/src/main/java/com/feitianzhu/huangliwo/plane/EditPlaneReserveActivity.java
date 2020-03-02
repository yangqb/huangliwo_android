package com.feitianzhu.huangliwo.plane;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.feitianzhu.huangliwo.view.CustomPlaneProtocolView;
import com.feitianzhu.huangliwo.view.CustomTicketPriceDetailView;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EditPlaneReserveActivity extends BaseActivity {
    private static final int REQUEST_CODE = 100;
    public static final String PLANE_TYPE = "plane_type";
    private int type;
    private SelectPassengerAdapter mAdapter;
    private List<Integer> list = new ArrayList<>();
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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SelectPassengerAdapter(list);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.left_button, R.id.cancel_change, R.id.rl_plane_info, R.id.luggage_buyTicket_notice, R.id.ticketPrice_detail, R.id.selectUser, R.id.tvReserveNotice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.cancel_change:
                new XPopup.Builder(EditPlaneReserveActivity.this)
                        .enableDrag(false)
                        .asCustom(new CustomCancelChangePopView(this
                        ).setType(type).setLuggage(false)).show();
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
            case R.id.selectUser:
                Intent intent = new Intent(EditPlaneReserveActivity.this, PassengerListActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.tvReserveNotice:
                Integer[] integers = new Integer[]{R.mipmap.k04_04yuding1, R.mipmap.k04_04yuding2, R.mipmap.k04_04yuding3};
                new XPopup.Builder(EditPlaneReserveActivity.this)
                        .enableDrag(false)
                        .asCustom(new CustomPlaneProtocolView(this
                        ).setTitle("机票预订须知").setData(Arrays.asList(integers))).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                int num = data.getIntExtra(PassengerListActivity.SELECT_PASSENGER, 0);
                for (int i = 0; i < num; i++) {
                    list.add(i);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
