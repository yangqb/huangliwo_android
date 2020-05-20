package com.feitianzhu.huangliwo.travel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseBindingActivity;
import com.feitianzhu.huangliwo.core.network.ApiCallBack;
import com.feitianzhu.huangliwo.databinding.ActivityTraveDetailBinding;
import com.feitianzhu.huangliwo.travel.adapter.Distance1Adapter;
import com.feitianzhu.huangliwo.travel.adapter.DistanceAdapter;
import com.feitianzhu.huangliwo.travel.base.BaseTravelRequest;
import com.feitianzhu.huangliwo.travel.model.OilStationsDetailBean;
import com.feitianzhu.huangliwo.travel.request.OilStationsDetailRequest;
import com.feitianzhu.huangliwo.travel.request.OilStationsRequest;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TraveDetailActivity extends BaseBindingActivity {

    private ActivityTraveDetailBinding dataBinding;
    private Distance1Adapter distanceAdapter;
    private ArrayList<String> oilPriceArrList = new ArrayList<>();
    private Distance1Adapter distanceAdapter1;

    @Override
    public void bindingView() {
        dataBinding = DataBindingUtil.setContentView(TraveDetailActivity.this, R.layout.activity_trave_detail);
        dataBinding.setViewModel(this);
    }

    public String n = "", n1 = "", n2 = "";

    @Override
    public void init() {
        distanceAdapter = new Distance1Adapter(null);
        dataBinding.oilClass.setLayoutManager(new GridLayoutManager(TraveDetailActivity.this, 4));
        dataBinding.oilClass.setAdapter(distanceAdapter);


//        distanceAdapter1 = new Distance1Adapter(strings1);
        dataBinding.oilLevel.setLayoutManager(new GridLayoutManager(this, 4));
        dataBinding.oilLevel.setAdapter(distanceAdapter1);

        OilStationsDetailRequest oilStationsDetailRequest = new OilStationsDetailRequest();
        oilStationsDetailRequest.gasIds = "";
        oilStationsDetailRequest.phone = SPUtils.getString(this, Constant.SP_PHONE);
        oilStationsDetailRequest.call(new ApiCallBack<List<OilStationsDetailBean>>() {
            @Override
            public void onAPIResponse(List<OilStationsDetailBean> response) {
                if (response != null && response.size() > 0) {
                    OilStationsDetailBean oilStationsDetailBean = response.get(0);
                    dataBinding.name.setText(oilStationsDetailBean.getGasName());
                    dataBinding.value.setText("￥0");
                    dataBinding.downValue.setText("$0");
                    List<OilStationsDetailBean.OilPriceListBean> oilPriceList = oilStationsDetailBean.getOilPriceList();
                    if (oilPriceList != null && oilPriceList.size() > 0) {
                        oilPriceArrList.add("汽油");
                        oilPriceArrList.add("柴油");
                        distanceAdapter.setNewData(oilPriceArrList);
                        distanceAdapter.notifyDataSetChanged();
                    }
                    oilPriceList.get(0).getGunNos();
                    ArrayList<String> strings1 = new ArrayList<>();
                    strings1.add("#92");
                    strings1.add("#95");
                }
            }

            @Override
            public void onAPIError(int errorCode, String errorMsg) {

            }
        });


        ArrayList<String> strings2 = new ArrayList<>();
        strings2.add("1号");
        strings2.add("2号");
        strings2.add("3号");
        strings2.add("4号");
        strings2.add("5号");
        strings2.add("6号");
        strings2.add("7号");
        strings2.add("8号");
        strings2.add("9号");
        strings2.add("10号");
        Distance1Adapter distanceAdapter2 = new Distance1Adapter(strings2);
        dataBinding.gun.setLayoutManager(new GridLayoutManager(this, 4));

        dataBinding.gun.setAdapter(distanceAdapter2);

        distanceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                n = oilPriceArrList.get(position);
                distanceAdapter.chengtextcolor(position);
                distanceAdapter.notifyDataSetChanged();
                distanceAdapter1.chengtextcolor(-1);
                distanceAdapter2.chengtextcolor(-1);
                distanceAdapter1.notifyDataSetChanged();
                distanceAdapter2.notifyDataSetChanged();
                Log.e("TAG", "init: " + n2 + ".." + n1 + ".." + ".." + n);

            }
        });
        distanceAdapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!StringUtils.isEmpty(n)) {
//                    n1 = strings1.get(position);
                    distanceAdapter1.chengtextcolor(position);
                    distanceAdapter1.notifyDataSetChanged();
                    distanceAdapter2.chengtextcolor(-1);
                    distanceAdapter2.notifyDataSetChanged();

                }
                Log.e("TAG", "init: " + n2 + ".." + n1 + ".." + ".." + n);

            }
        });
        distanceAdapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!StringUtils.isEmpty(n1)) {
                    n2 = strings2.get(position);
                    distanceAdapter2.chengtextcolor(position);
                    distanceAdapter2.notifyDataSetChanged();
                }
                Log.e("TAG", "init: " + n2 + ".." + n1 + ".." + ".." + n);

            }
        });
    }
}