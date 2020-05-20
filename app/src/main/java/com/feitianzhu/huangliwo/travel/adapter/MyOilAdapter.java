package com.feitianzhu.huangliwo.travel.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.shop.ShopMerchantsDetailActivity;
import com.feitianzhu.huangliwo.travel.bean.OilListBean;
import com.feitianzhu.huangliwo.view.CustomRefundView;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.List;

public class MyOilAdapter extends BaseQuickAdapter <OilListBean, BaseViewHolder>{
    private String posion1;
    private List<String> mapList = new ArrayList<>();

    public MyOilAdapter( @Nullable List<OilListBean> data) {
        super(R.layout.oil_adapter_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OilListBean item) {
        helper.setText(R.id.oilname,item.getGasName());
        helper.setText(R.id.oiladdress,item.getGasAddress());
        helper.setText(R.id.oilprice,"￥"+item.getOilPriceList().get(0).getPriceYfq());
        String[] split = posion1.split("#");
        //helper.setText(R.id.oildiscountprice,format);
        for (int i = 0; i <item.getOilPriceList().size() ; i++) {
            if (item.getOilPriceList().get(i).getOilNo()==Integer.valueOf(split[0])){
                /*String priceOfficial = item.getOilPriceList().get(i).getPriceOfficial();
                String priceYfq = item.getOilPriceList().get(i).getPriceYfq();*/
                String priceOfficial = item.getOilPriceList().get(0).getPriceOfficial();
                String priceYfq = item.getOilPriceList().get(0).getPriceYfq();
                double v = Double.valueOf(priceOfficial) - Double.valueOf(priceYfq);
                java.text.DecimalFormat   df   =new   java.text.DecimalFormat("0.00");
                String format = df.format(v);
                Log.i("initwork", "convert: "+split[0]+priceYfq+priceOfficial+format);
                helper.setText(R.id.oilprice,"￥"+priceYfq);
                helper.setText(R.id.oildiscountprice,String.valueOf(v));
            }
            return;
        }
        helper.setText(R.id.distancetext,item.getDistanceStr());
        ImageView oilnavigation = helper.getView(R.id.oilnavigation);

        oilnavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mapList.size() <= 0) {
                    ToastUtils.show("请先安装百度地图或高德地图");
                    return;
                } else {
                    new XPopup.Builder(mContext)
                            .asCustom(new CustomRefundView(mContext)
                                    .setData(mapList)
                                    .setOnItemClickListener(new CustomRefundView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            if ("百度地图".equals(mapList.get(position))) {
                                                Intent i1 = new Intent();
                                               // i1.setData(Uri.parse("baidumap://map/geocoder?src=andr.baidu.openAPIdemo&address=" + merchantsBean.getCityName() + merchantsBean.getAreaName() + merchantsBean.getDtlAddr()));
                                                mContext.startActivity(i1);
                                            } else if ("高德地图".equals(mapList.get(position))) {
                                                Intent intent_gdmap = new Intent();
                                                intent_gdmap.setAction("android.intent.action.VIEW");
                                                intent_gdmap.setPackage("com.autonavi.minimap");
                                                intent_gdmap.addCategory("android.intent.category.DEFAULT");
                                                //intent_gdmap.setData(Uri.parse("androidamap://poi?sourceApplication=com.feitianzhu.huangliwo&keywords=" + merchantsBean.getCityName() + merchantsBean.getAreaName() + merchantsBean.getDtlAddr() + "&dev=0"));
                                                mContext.startActivity(intent_gdmap);
                                            }
                                        }
                                    }))
                            .show();
                }
            }
        });

    }
    public void chengtextcolor1(String posion){
        this.posion1=posion;
    }
}
