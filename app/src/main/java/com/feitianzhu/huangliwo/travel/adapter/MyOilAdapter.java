package com.feitianzhu.huangliwo.travel.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.shop.ShopMerchantsDetailActivity;
import com.feitianzhu.huangliwo.travel.bean.OilListBean;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MyOilAdapter extends BaseQuickAdapter<OilListBean, BaseViewHolder> {
    private String posion1;
    private List<String> mapList = new ArrayList<>();

    public MyOilAdapter(@Nullable List<OilListBean> data) {
        super(R.layout.oil_adapter_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OilListBean item) {
        helper.setText(R.id.oilname, item.getGasName());
        helper.setText(R.id.oiladdress, item.getGasAddress());
        helper.setText(R.id.oilprice, "￥" + item.getOilPriceList().get(0).getPriceYfq());
        String[] split = posion1.split("#");
        String gasAddress = item.getGasAddress();
        double gasAddressLatitude = item.getGasAddressLatitude();
        double gasAddressLongitude = item.getGasAddressLongitude();
        //helper.setText(R.id.oildiscountprice,format);
        for (int i = 0; i < item.getOilPriceList().size(); i++) {
            if (item.getOilPriceList().get(i).getOilNo() == Integer.valueOf(split[0])) {
                /*String priceOfficial = item.getOilPriceList().get(i).getPriceOfficial();
                String priceYfq = item.getOilPriceList().get(i).getPriceYfq();*/
                String priceOfficial = item.getOilPriceList().get(0).getPriceOfficial();
                String priceYfq = item.getOilPriceList().get(0).getPriceYfq();
                double v = Double.valueOf(priceOfficial) - Double.valueOf(priceYfq);
                java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
                String format = df.format(v);
                Log.i("initwork", "convert: " + split[0] + priceYfq + priceOfficial + format);
                helper.setText(R.id.oilprice, "￥" + priceYfq);
                helper.setText(R.id.oildiscountprice, format);
                break;
            }
        }
        helper.setText(R.id.distancetext, item.getDistanceStr());
        ImageView oilnavigation = helper.getView(R.id.oilnavigation);

        oilnavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.popup_layout_itemnavigation, null);
              PopupWindow  popupWindow = new PopupWindow(view,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setAnimationStyle(R.style.MyPopupWindow_anim_style);
               TextView gaode_map = view.findViewById(R.id.gaode_map);
                TextView  baidu_map = view.findViewById(R.id.baidu_map);
                TextView tencent_map = view.findViewById(R.id.tencent_map);
                tencent_map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                gaode_map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isInstallByread("com.autonavi.minimap")) {
                            Intent intent = new Intent(
                                    "android.intent.action.VIEW",
                                    android.net.Uri.parse(
                                            "androidamap://route?sourceApplication=应用名称" + "&dlat="+ String.valueOf(gasAddressLatitude)//终点的经度
                                                    + "&dlon="+ String.valueOf(gasAddressLongitude)//终点的纬度
                                                    +"&dname="+gasAddress
                                                    + "&dev=0" + "&t=1"));
                            mContext.startActivity(intent);
                        } else {
                            Toast.makeText(mContext, "没有安装高德地图客户端，请先下载该地图应用", Toast.LENGTH_SHORT).show();
                        }
                        popupWindow.dismiss();
                    }
                });
                baidu_map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isInstallByread("com.baidu.BaiduMap")) {
                            Intent intent = null;
                            try {
                                intent = Intent.getIntent("intent://map/direction?" +
                                        "destination=latlng:" +gasAddressLatitude  + "," + gasAddressLongitude + "|name:" +gasAddress+  //终点
                                        "&mode=driving&" +     //导航路线方式
                                        "&src=appname#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                                mContext.startActivity(intent); // 启动调用
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(mContext, "没有安装百度地图客户端，请先下载该地图应用", Toast.LENGTH_SHORT).show();
                        }
                        popupWindow.dismiss();
                    }
                });
                popupWindow.showAtLocation(helper.getView(R.id.oilname), Gravity.BOTTOM, 0,0);
            }
        });

    }
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public void chengtextcolor1(String posion) {
        this.posion1 = posion;
    }
}