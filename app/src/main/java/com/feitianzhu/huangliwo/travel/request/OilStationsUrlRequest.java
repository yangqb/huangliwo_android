package com.feitianzhu.huangliwo.travel.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.core.network.BaseTravelUrlRequest;
import com.feitianzhu.huangliwo.travel.bean.OilListBean;

import java.util.List;

/**
 * Created by bch on 2020/5/20
 */
public class OilStationsUrlRequest extends BaseTravelUrlRequest {
    public String longitude;
    public String latitude;
    public String accessToken;
    public String userId;
    public int kilometre;
    public int oilNum;
    public int limitNum;
    public int curPage;

    @Override
    public String getAPIName() {
        return "fleetin/getOilStations";
    }

    public OilStationsUrlRequest(String longitude, String latitude, int kilometre, int oilNum, int limitNum, int curPage) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.kilometre = kilometre;
        this.oilNum = oilNum;
        this.limitNum = limitNum;
        this.curPage = curPage;
    }

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return super.appendParams(builder.append("longitude", longitude)
                .append("latitude", latitude).append("kilometre", kilometre)
                .append("oilNum", oilNum).append("limitNum", limitNum)
                .append("curPage", curPage)
                .append("userId", userId)
                .append("accessToken",accessToken)

        );
    }

    @Override
    public boolean usePost() {
        return false;
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<List<OilListBean>>() {
        };
    }
}
