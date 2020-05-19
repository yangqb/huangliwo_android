package com.feitianzhu.huangliwo.travel;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;

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
    private PopupWindow popupWindow;
    private String all = "";
    private String num;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_travel_home;
    }

    @Override
    protected void initView() {
        titleName.setText("团油");
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
                TextView oilnameone = view.findViewById(R.id.distanceone);
                TextView oilnametwodiesel = view.findViewById(R.id.distancetwo);
                TextView oilnametendiesel = view.findViewById(R.id.distancethree);
                TextView oilnametwo = view.findViewById(R.id.distancefour);
                oilnameone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        all = "5km";
                        distance.setText(all);
                        num =all.substring(0,all.length()-1);
                        int page=0;
                        popupWindow.dismiss();
                    }
                });
                oilnametwodiesel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        all = "10km";
                        distance.setText(all);
                        num =all.substring(0,all.length()-1);
                        int page=0;
                        popupWindow.dismiss();
                    }
                });
                oilnametendiesel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        all = "20km";
                        distance.setText(all);
                        num =all.substring(0,all.length()-1);
                        int page=0;
                        popupWindow.dismiss();
                    }
                });
                oilnametwo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        all = "30km";
                        distance.setText(all);
                        num =all.substring(0,all.length()-1);
                        int page=0;
                        popupWindow.dismiss();
                    }
                });
                //popupWindow.setAnimationStyle(R.style.MyPopupWindow_anim_style);
                // popupWindow.showAtLocation(oilnumber, Gravity.TOP, 1,1);
                popupWindow.showAsDropDown(oilnumber);
                break;
            case R.id.oilnumber:
                view = LayoutInflater.from(TravelHomeActivity.this).inflate(
                        R.layout.popup_item_oil, null);
                popupWindow = new PopupWindow(view,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, true);
                break;
        }
    }
}
