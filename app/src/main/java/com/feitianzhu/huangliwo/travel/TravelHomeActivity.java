package com.feitianzhu.huangliwo.travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.core.network.ApiCallBack;
import com.feitianzhu.huangliwo.model.MyPoint;
import com.feitianzhu.huangliwo.travel.adapter.Distance1Adapter;
import com.feitianzhu.huangliwo.travel.adapter.DistanceAdapter;
import com.feitianzhu.huangliwo.travel.adapter.MyOilAdapter;
import com.feitianzhu.huangliwo.travel.bean.OilListBean;
import com.feitianzhu.huangliwo.travel.request.OilStationsRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.baidu.location.h.a.s;

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
    private String longitude;
    private String latitude;
    private String dinstancenumber;
    private String oilnumbersum;
    private String[] kms;
    private String[] split;

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
    }

    @OnClick({R.id.left_button, R.id.right_button})
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:
                startActivity(new Intent(this,TraveFormActivity.class));
                break;
        }
    }

    @Override
    protected void initData() {
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
        strings1.add("0#");
        initoil();
    }

    private void initoil() {
         dinstancenumber = (String) distance.getText();
        oilnumbersum = (String) oilnumber.getText();
        if (Constant.mPoint != null) {
            MyPoint myPoint = Constant.mPoint;
            longitude = myPoint.longitude+"";
            latitude = myPoint.latitude+"";
        }
        initwork(dinstancenumber,oilnumbersum);
    }
    @OnClick({R.id.distance, R.id.oilnumber})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.distance:
               String text1 = (String) distance.getText();
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
                        String dinstancenumber = dinstance.get(position);
                        distance.setText(dinstancenumber);
                        oilnumbersum = (String) oilnumber.getText();
                        initwork(dinstancenumber,oilnumbersum);
                        popupWindow.dismiss();
                    }
                });
                dadapter.chengtextcolor1(text1);
                popupWindow.showAsDropDown(distancerela);
                break;
            case R.id.oilnumber:
               String text = (String) oilnumber.getText();
                view = LayoutInflater.from(TravelHomeActivity.this).inflate(
                        R.layout.popup_item_oilnumber, null);
                popupWindow = new PopupWindow(view,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, true);
                RecyclerView oilClass = view.findViewById(R.id.oilClass);
                RecyclerView oilLevel = view.findViewById(R.id.oilLevel);
                oilClass.setLayoutManager(new GridLayoutManager(this, 4));
                Distance1Adapter distanceAdapter = new Distance1Adapter(strings);
                oilClass.setAdapter(distanceAdapter);
                distanceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        distanceAdapter.chengtextcolor(position);
                        distanceAdapter.notifyDataSetChanged();
                        String oilnumbersum = strings.get(position);
                        oilnumber.setText(oilnumbersum);
                        dinstancenumber = (String) distance.getText();
                        initwork(dinstancenumber,oilnumbersum);
                        popupWindow.dismiss();
                    }
                });

                distanceAdapter.chengtextcolor1(oilnumbersum);
                distanceAdapter.notifyDataSetChanged();
                oilLevel.setLayoutManager(new GridLayoutManager(this, 4));
                Distance1Adapter distanceAdapter1 = new Distance1Adapter(strings1);
                oilLevel.setAdapter(distanceAdapter1);
                distanceAdapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        distanceAdapter1.chengtextcolor(position);
                        distanceAdapter1.notifyDataSetChanged();
                        String oilnumbersum = strings1.get(position);
                        oilnumber.setText(oilnumbersum);
                        dinstancenumber = (String) distance.getText();
                        initwork(dinstancenumber,oilnumbersum);
                        popupWindow.dismiss();
                    }
                });
                distanceAdapter1.chengtextcolor1(oilnumbersum);
                distanceAdapter1.notifyDataSetChanged();
                popupWindow.showAsDropDown(distancerela);
                break;
        }
    }

    private void initwork(String dinstancenumber,String oilnumbersum) {
        kms = dinstancenumber.split("km");
        split = oilnumbersum.split("#");
        oilrecy.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        OilStationsRequest oilStationsRequest = new OilStationsRequest(longitude,latitude,Integer.valueOf(kms[0]),Integer.valueOf(split[0]),20,1);
        oilStationsRequest.call(new ApiCallBack<List<OilListBean>>() {
            @Override
            public void onAPIResponse(List<OilListBean> response) {
                        if (response!=null&&response.size()>0){
                            MyOilAdapter myoiladapter=new  MyOilAdapter(response);
                            oilrecy.setAdapter(myoiladapter);
                            myoiladapter.chengtextcolor1(oilnumbersum);
                            myoiladapter.notifyDataSetChanged();
                        }
            }

            @Override
            public void onAPIError(int errorCode, String errorMsg) {

            }
        });
    }


}
