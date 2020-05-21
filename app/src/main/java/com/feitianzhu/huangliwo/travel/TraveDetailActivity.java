package com.feitianzhu.huangliwo.travel;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.activity.BaseBindingActivity;
import com.feitianzhu.huangliwo.core.network.ApiCallBack;
import com.feitianzhu.huangliwo.core.network.ApiLifeCallBack;
import com.feitianzhu.huangliwo.databinding.ActivityTraveDetailBinding;
import com.feitianzhu.huangliwo.travel.adapter.Distance1Adapter;
import com.feitianzhu.huangliwo.travel.adapter.DistanceGunAdapter;
import com.feitianzhu.huangliwo.travel.adapter.DistanceOilInfoAdapter;
import com.feitianzhu.huangliwo.travel.bean.OilListBean;
import com.feitianzhu.huangliwo.travel.bean.OilStationsDetailBean;
import com.feitianzhu.huangliwo.travel.request.OilStationsDetailRequest;
import com.feitianzhu.huangliwo.travel.request.OilTimeRequest;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.feitianzhu.huangliwo.utils.doubleclick.SingleClick;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.feitianzhu.huangliwo.common.Constant.SP_PHONE;

public class TraveDetailActivity extends BaseBindingActivity {

    private ActivityTraveDetailBinding dataBinding;
    private List<OilStationsDetailBean> response;

    //油类
    private Distance1Adapter distanceAdapter;
    //油号
    private DistanceOilInfoAdapter distanceAdapter1;

    private DistanceGunAdapter distanceAdapter2;


    private int position;
    private OilListBean oilListBean;

    @Override
    public void bindingView() {
        String gson = getIntent().getStringExtra("GSON");
        Gson gson1 = new Gson();
        oilListBean = gson1.fromJson(gson, OilListBean.class);
        dataBinding = DataBindingUtil.setContentView(TraveDetailActivity.this, R.layout.activity_trave_detail);
        dataBinding.setViewModel(this);
    }

    public String n = "", n1 = "", n2 = "";

    public static void toTraveDetailActivity(AppCompatActivity appCompatActivity, OilListBean oilListBean) {
        String s = new Gson().toJson(oilListBean);
        Intent intent = new Intent(appCompatActivity, TraveDetailActivity.class);
        intent.putExtra("GSON", s);
        appCompatActivity.startActivity(intent);
    }
    @Override
    public void init() {
        if (oilListBean != null) {
            dataBinding.submit.setEnabled(false);
            dataBinding.name.setText(oilListBean.getGasName());
            Glide.with(this)
                    .load(oilListBean.getGasLogoSmall())
                    .into(dataBinding.imageView8);
            dataBinding.value.setText("￥0");
            dataBinding.downValue.setText("$0");
            dataBinding.address.setText(oilListBean.getGasAddress());
            dataBinding.str.setText(oilListBean.getDistanceStr());

        }


        distanceAdapter = new Distance1Adapter(null);
        dataBinding.oilClass.setLayoutManager(new GridLayoutManager(TraveDetailActivity.this, 4));
        dataBinding.oilClass.setAdapter(distanceAdapter);


        distanceAdapter1 = new DistanceOilInfoAdapter(null);
        dataBinding.oilLevel.setLayoutManager(new GridLayoutManager(this, 4));
        dataBinding.oilLevel.setAdapter(distanceAdapter1);


        distanceAdapter2 = new DistanceGunAdapter(null);
        dataBinding.gun.setLayoutManager(new GridLayoutManager(this, 4));

        dataBinding.gun.setAdapter(distanceAdapter2);


        OilStationsDetailRequest oilStationsDetailRequest = new OilStationsDetailRequest();
        oilStationsDetailRequest.isShowLoading = true;
        oilStationsDetailRequest.gasIds = oilListBean.getGasId();
        oilStationsDetailRequest.phone = SPUtils.getString(this, SP_PHONE);
        oilStationsDetailRequest.call(new ApiLifeCallBack<List<OilStationsDetailBean>>() {

            @Override
            public void onStart() {
                showloadDialog("");
            }

            @Override
            public void onFinsh() {
                goneloadDialog();
            }

            @Override
            public void onAPIResponse(List<OilStationsDetailBean> response) {
                TraveDetailActivity.this.response = response;
                if (response != null && response.size() > 0) {
                    ArrayList<String> oilPriceArrList = new ArrayList<>();
                    for (OilStationsDetailBean oilPriceList : response) {
                        oilPriceArrList.add(oilPriceList.getOilTypeString());
                    }
                    distanceAdapter.setNewData(oilPriceArrList);
                    n = oilPriceArrList.get(0);
                    distanceAdapter.chengtextcolor(0);
                    distanceAdapter.notifyDataSetChanged();

                    List<OilStationsDetailBean.OilInfoBean> oilInfo = response.get(0).getOilInfo();


                    if (oilInfo != null && oilInfo.size() > 0) {

                        distanceAdapter1.setNewData(oilInfo);
                        n1 = oilInfo.get(0).getOilName();
                        distanceAdapter1.chengtextcolor(0);
                        distanceAdapter1.notifyDataSetChanged();


                        OilStationsDetailBean.OilInfoBean oilInfoBean = oilInfo.get(0);
                        dataBinding.value.setText(oilInfoBean.getPriceYfq() + "");
                        double priceYfq = oilInfoBean.getPriceYfq();
                        double priceOfficial = oilInfoBean.getPriceOfficial();
                        double v = priceOfficial - priceYfq;
                        String format = new DecimalFormat("0.00").format(v);
                        dataBinding.downValue.setText(format);

                        List<OilStationsDetailBean.OilInfoBean.GunNosBean> gunNos = oilInfoBean.getGunNos();
                        if (gunNos != null && gunNos.size() > 0) {
                            distanceAdapter2.setNewData(gunNos);
                            n2 = "";
                            distanceAdapter2.chengtextcolor(-1);
                            distanceAdapter2.notifyDataSetChanged();
                        }
                    }


                }
            }

            @Override
            public void onAPIError(int errorCode, String errorMsg) {

            }
        });


        distanceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TraveDetailActivity.this.position = position;
                n = distanceAdapter.getData().get(position);
                distanceAdapter.chengtextcolor(position);
                distanceAdapter.notifyDataSetChanged();
                distanceAdapter2.chengtextcolor(-1);
                distanceAdapter2.notifyDataSetChanged();

                List<OilStationsDetailBean.OilInfoBean> oilInfo = response.get(position).getOilInfo();
                if (oilInfo != null && oilInfo.size() > 0) {
                    distanceAdapter1.setNewData(oilInfo);
                    distanceAdapter1.chengtextcolor(0);
                    distanceAdapter1.notifyDataSetChanged();
                    n1 = distanceAdapter1.getData().get(0).getOilName();
                }


                if (oilInfo != null && oilInfo.size() > 0) {
                    OilStationsDetailBean.OilInfoBean oilInfoBean = oilInfo.get(0);
                    List<OilStationsDetailBean.OilInfoBean.GunNosBean> gunNos = oilInfoBean.getGunNos();
                    if (gunNos != null && gunNos.size() > 0) {
                        distanceAdapter2.setNewData(gunNos);
                    }
                }
                distanceAdapter2.chengtextcolor(-1);
                distanceAdapter2.notifyDataSetChanged();
                n2 = "";
                Log.e("TAG", "init: " + n2 + ".." + n1 + ".." + ".." + n);
                dataBinding.submit.setEnabled(false);

            }
        });
        distanceAdapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!StringUtils.isEmpty(n)) {
                    dataBinding.submit.setEnabled(false);

                    n1 = distanceAdapter1.getData().get(position).getOilName();
                    distanceAdapter1.chengtextcolor(position);
                    distanceAdapter1.notifyDataSetChanged();
                    OilStationsDetailBean.OilInfoBean oilInfoBean1 = distanceAdapter1.getData().get(position);

                    dataBinding.value.setText(oilInfoBean1.getPriceYfq() + "");
                    double priceYfq = oilInfoBean1.getPriceYfq();
                    double priceOfficial = oilInfoBean1.getPriceOfficial();
                    double v = priceOfficial - priceYfq;
                    dataBinding.downValue.setText(new DecimalFormat("0.00").format(v));

                    List<OilStationsDetailBean.OilInfoBean> oilInfo = response.get(TraveDetailActivity.this.position).getOilInfo();
                    if (oilInfo != null && oilInfo.size() > 0) {
                        OilStationsDetailBean.OilInfoBean oilInfoBean = oilInfo.get(position);
                        List<OilStationsDetailBean.OilInfoBean.GunNosBean> gunNos = oilInfoBean.getGunNos();
                        if (gunNos != null && gunNos.size() > 0) {
                            distanceAdapter2.setNewData(gunNos);
                            distanceAdapter2.chengtextcolor(-1);
                            distanceAdapter2.notifyDataSetChanged();
                            n2 = "";
                        }
                    }


                }

                Log.e("TAG", "init: " + n2 + ".." + n1 + ".." + ".." + n);

            }
        });
        distanceAdapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!StringUtils.isEmpty(n1)) {
                    n2 = distanceAdapter2.getData().get(position).getGunNo();
                    distanceAdapter2.chengtextcolor(position);
                    distanceAdapter2.notifyDataSetChanged();
                    dataBinding.submit.setEnabled(true);

                }
                Log.e("TAG", "init: " + n2 + ".." + n1 + ".." + ".." + n);

            }
        });
        dataBinding.submit.setOnClickListener(new View.OnClickListener() {
            @SingleClick()
            @Override
            public void onClick(View v) {
                if (!StringUtils.isEmpty(n1) && !StringUtils.isEmpty(n2)) {
                    OilTimeRequest oilTimeRequest = new OilTimeRequest();
                    oilTimeRequest.phone = SPUtils.getString(TraveDetailActivity.this, SP_PHONE);
                    oilTimeRequest.platformId = "98647229";
                    oilTimeRequest.call(new ApiCallBack<String>() {
                        @Override
                        public void onAPIResponse(String response) {
                            Log.e("TAG", "onAPIResponse: " + response);

//                            http://test-mcs.czb365.com/services/v3/begin/getSecretCode?platformId=98647229&phone=13671192850
                            Web1Activity.toWeb1Activity(TraveDetailActivity.this, "https://test-open.czb365.com/redirection/todo/?platformType=98647229&authCode="
                                    + response + "&gasId=" + oilListBean.getGasId() + "&gunNo=" + n2);

                        }

                        @Override
                        public void onAPIError(int errorCode, String errorMsg) {

                        }
                    });
                }
//                ToastUtils.show("init: " + n2 + ".." + n1 + ".." + ".." + n);
//
            }
        });
    }
}