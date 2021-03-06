package com.feitianzhu.huangliwo.utils;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.feitianzhu.huangliwo.App;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.impl.Manager;
import com.feitianzhu.huangliwo.model.LocationPost;
import com.feitianzhu.huangliwo.model.MyPoint;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by jiangdikai on 2017/3/9.
 */

public class LocationUtils implements Manager {
    /**
     * 类级内部类 也就是静态的成员式内部类 该内部类的实例与外部类的实例没有依赖
     * 而且只有被调用的时候才会被装载，从而实现延迟加载
     */
    private static class SingletonHolder {
        //静态初始化器 由虚拟机保证线程安全
        private static LocationUtils instance = new LocationUtils();
    }

    private LocationUtils() {
    }

    public static LocationUtils getInstance() {
        return SingletonHolder.instance;
    }

    private BDLocationListener myListener = new MyLocationListener();

    private LocationClient mLocationClient;

    @Override
    public void start() {
        initLocationClient();
        mLocationClient.setLocOption(initLocation());
        mLocationClient.start();
    }

    @Override
    public void stop() {
        mLocationClient.stop();
    }

    private void initLocationClient() {
        if (null == mLocationClient)
            mLocationClient = new LocationClient(App.getAppContext());
        //声明LocationClient类
        if (null == myListener)
            myListener = new MyLocationListener();

        mLocationClient.registerLocationListener(myListener);

    }

    private LocationClientOption initLocation() {

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 2000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        return option;

    }

    private class MyLocationListener implements BDLocationListener {


        @Override
        public void onReceiveLocation(BDLocation location) {

            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间

            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型

            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());    //获取纬度信息

            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());    //获取经度信息

            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度

            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                Constant.mPoint = new MyPoint(location.getLongitude(), location.getLatitude());
                Constant.mCity = location.getCity();
                Constant.mProvince = location.getProvince();
                EventBus.getDefault().postSticky(new LocationPost(true));
              /*  if (isDone && !isStarted)
                    start();*/
                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时

                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数

                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米

                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度

                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                Constant.mPoint = new MyPoint(location.getLongitude(), location.getLatitude());
                Constant.mCity = location.getCity();
                Constant.mProvince = location.getProvince();
                location.getProvince();
                EventBus.getDefault().postSticky(new LocationPost(true));
//                if (isDone && !isStarted)
//                    start();
                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息

                sb.append("\ndescribe : ");
                sb.append("网络定位成功");

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                Constant.mPoint = new MyPoint(location.getLongitude(), location.getLatitude());
                Constant.mCity = location.getCity();
                Constant.mProvince = location.getProvince();
                EventBus.getDefault().postSticky(new LocationPost(true));
//                if (isDone && !isStarted)
//                    start();
                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");

            } else if (location.getLocType() == BDLocation.TypeServerError) {
                //Constant.mPoint = null;
                //Constant.mCity = "";
                EventBus.getDefault().postSticky(new LocationPost(false));
//                UIShowGpsErro();
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                //Constant.mPoint = null;
                //Constant.mCity = "";
                EventBus.getDefault().postSticky(new LocationPost(false));
//                UIShowGpsErro();
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                //Constant.mPoint = null;
                //Constant.mCity = "";
                EventBus.getDefault().postSticky(new LocationPost(false));
//                UIShowGpsErro();
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }

            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息

            List<Poi> list = location.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }

            Log.i("BaiduLocationApiDem", sb.toString());
            mLocationClient.stop();
        }

    }
}
