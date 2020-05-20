package com.feitianzhu.huangliwo.travel;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.travel.adapter.Distance1Adapter;
import com.feitianzhu.huangliwo.travel.adapter.DistanceAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.huangliwo.travel
 * user: yangqinbo
 * date: 2020/4/9
 * time: 14:45
 * email: 694125155@qq.com
 */
public class TravelHomeActivity extends BaseActivity {

    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.distance)
    TextView distance;
    @BindView(R.id.oilnumber)
    TextView oilnumber;
    @BindView(R.id.oilrecy)
    RecyclerView oilrecy;
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.distancerela)
    RelativeLayout distancerela;
    @BindView(R.id.oilnumberrela)
    RelativeLayout oilnumberrela;
    private PopupWindow popupWindow;
    private String all = "";
    private String num;
    private List<String> dinstance = new ArrayList<>();
    private RecyclerView dinstancerecy;
    private ArrayList<String> strings;
    private ArrayList<String> strings1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_travel_home;
    }

    @Override
    protected void initView() {
        rightText.setVisibility(View.VISIBLE);
        rightImg.setVisibility(View.VISIBLE);
        titleName.setText("加油优惠");
        rightText.setText("订单");
        //Glide.with(this).load(R.mipmap.lvyou).into(GlideUtils.getImageView2(this, R.mipmap.lvyou, imageView));
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        dinstance.add("5km");
        dinstance.add("10km");
        dinstance.add("15km");
        dinstance.add("20km");
        dinstance.add("30km");
        dinstance.add("40km");
        strings = new ArrayList<>();
        strings.add("90#");
        strings.add("92#");
        strings.add("95#");
        strings.add("98#");
        strings.add("101#");
        strings1 = new ArrayList<>();
        strings1.add("-40#");
        strings1.add("-35#");
        strings1.add("-30#");
        strings1.add("-20#");
        strings1.add("-10#");
        strings1.add("-0#");
    }
    @OnClick({R.id.distance, R.id.oilnumber})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.distance:
                view = LayoutInflater.from(TravelHomeActivity.this).inflate(
                        R.layout.popup_item_distance, null);
                popupWindow = new PopupWindow(view,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, true);
                dinstancerecy = view.findViewById(R.id.dinstancerecy);
                dinstancerecy.setLayoutManager(new GridLayoutManager(this, 4));
                DistanceAdapter dadapter = new DistanceAdapter(dinstance);
                dinstancerecy.setAdapter(dadapter);
                dadapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        dadapter.chengtextcolor(position);
                        dadapter.notifyDataSetChanged();
                        String s = dinstance.get(position);
                        distance.setText(s);
                        popupWindow.dismiss();
                    }
                });
                popupWindow.showAsDropDown(distancerela);
                break;
            case R.id.oilnumber:
                view = LayoutInflater.from(TravelHomeActivity.this).inflate(
                        R.layout.popup_item_oilnumber, null);
                popupWindow = new PopupWindow(view,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, true);
                dinstancerecy = view.findViewById(R.id.dinstancerecy);
            /*    dinstancerecy.setLayoutManager(new GridLayoutManager(this, 4));
                Distance1Adapter distanceAdapter = new Distance1Adapter(strings);
                dinstancerecy.setAdapter(distanceAdapter);
                Distance1Adapter distanceAdapter1 = new Distance1Adapter(strings1);
                dinstancerecy.setAdapter(distanceAdapter1);*/
                popupWindow.showAsDropDown(distancerela);
                break;
        }
    }
}
