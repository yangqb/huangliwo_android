package com.feitianzhu.huangliwo.core.network.networkcheck;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * Created by liusiyang on 2018/5/4.
 */

public class NetworkConnectChangedReceiver extends BroadcastReceiver {

    private static final String TAG = "NetworkConnect";
    //网络状态
    private NetWorkState networkStatus = NetWorkState.Null;

    /**
     * 没有网络连接
     */
    public static final NetWorkState NETWORK_STATUS_NO_CONNECTION = NetWorkState.NONE;
    /**
     * 其他连接(VPN,以太网等)
     */
    public static final NetWorkState NETWORK_STATUS_OTHERS = NetWorkState.VPN;
    /**
     * wifi连接
     */
    public static final NetWorkState NETWORK_STATUS_WIFI = NetWorkState.WIFI;
    /**
     * 手机流量连接
     */
    public static final NetWorkState NETWORK_STATUS_MOBILE = NetWorkState.GPRS;

    public NetworkChangedListener listener;
    public static NetworkConnectChangedReceiver receiver;
    //================== NetworkChangedListener ====================

    public interface NetworkChangedListener {
        public void onNetworkChanged(NetWorkState networkStatus);
    }

    //================== static method ====================

    public static void addNetworkConnectChangedListener(Context context, NetworkChangedListener listener) {
        if (receiver == null) {
            receiver = new NetworkConnectChangedReceiver();
        }
        receiver.listener = listener;
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        context.registerReceiver(receiver, filter);

        receiver.callBack(getNetworkStatus(context));
    }

    public static void removeNetworkConnectChangedListener(Context context) {
        context.unregisterReceiver(receiver);
    }

    /**
     * 网络连接状态
     */
    public static NetWorkState getNetworkStatus(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.isConnected()) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    return NETWORK_STATUS_WIFI;
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return NETWORK_STATUS_MOBILE;
                }
                return NETWORK_STATUS_OTHERS;
            } else {
                return NETWORK_STATUS_NO_CONNECTION;
            }
        } else {   // not connected to the internet
            return NETWORK_STATUS_NO_CONNECTION;
        }
    }

    /**
     * wifi开关是否打开
     */
    public static boolean isWifiConnected(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            return true;
        }
        return false;
    }

    //================== override ====================

    @Override
    public void onReceive(Context context, Intent intent) {

//        //监听wifi的打开与关闭，与wifi的连接无关
//        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
//            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
//            switch (wifiState) {
//                case WifiManager.WIFI_STATE_DISABLED:
//                    break;
//                case WifiManager.WIFI_STATE_DISABLING:
//
//                    break;
//                case WifiManager.WIFI_STATE_ENABLING:
//                    break;
//                case WifiManager.WIFI_STATE_ENABLED:
//                    break;
//                case WifiManager.WIFI_STATE_UNKNOWN:
//                    break;
//                default:
//                    break;
//
//
//            }
////            callBack(getNetworkStatus(context));
//        }

//        /*
//         *  这个监听wifi的连接状态即是否连上了一个有效无线路由
//         *  当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
//         *  在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播
//         *  当然刚打开wifi肯定还没有连接到有效的无线
//         */
//        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
//            Parcelable parcelableExtra = intent
//                    .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
//            if (null != parcelableExtra) {
//                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
//                NetworkInfo.State state = networkInfo.getState();
//                boolean isConnected = state == NetworkInfo.State.CONNECTED;
//                callBack(getNetworkStatus(context));
//            }
//        }

        /*
        这个监听网络连接的设置,包括wifi和移动数据的打开和关闭,最好用的还是这个监听。
        wifi如果打开，关闭，以及连接上可用的连接都会接到监听。
        这个广播的最大弊端是比上边两个广播的反应要慢
        */
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            callBack(getNetworkStatus(context));
        }
    }

    //================== private method ====================

    private void callBack(NetWorkState networkStatus) {
        if (this.networkStatus != networkStatus) {
            this.networkStatus = networkStatus;
            if (listener != null) {
                listener.onNetworkChanged(networkStatus);
            }
        }
    }
}











