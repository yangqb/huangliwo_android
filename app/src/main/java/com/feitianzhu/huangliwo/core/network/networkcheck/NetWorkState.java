package com.feitianzhu.huangliwo.core.network.networkcheck;

/**
 * 网络状态枚举
 * Created by bch on 2020/5/11
 */
public enum NetWorkState {
    WIFI,//Wi-Fi网络
    GPRS,//移动蜂窝网络
    NONE,//没有网络
    VPN,//其他连接(VPN,以太网等)
    Null//其他

}
