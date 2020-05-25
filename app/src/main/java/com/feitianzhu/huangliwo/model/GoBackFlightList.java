package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.plane
 * user: yangqinbo
 * date: 2020/3/17
 * time: 14:43
 * email: 694125155@qq.com
 */
public class GoBackFlightList implements Serializable {
    public int tof;//":"0",
    public int arf;//":"100",
    public GoBackFlight go;
    public GoBackFlight back;
    public GoBackPack pack;
    public String flightCodes;//":"HU7609_HU7602",
    public int minBarePrice;//":"540"
    public double zk;

}
