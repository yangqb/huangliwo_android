package com.feitianzhu.huangliwo.shop.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.shop.model.MerchantBean;
import com.feitianzhu.huangliwo.shop.model.ShopClassifyBean;

import java.util.List;


/**
 * Created by bch on 2020/5/18
 */
public class MerchantListRequest extends BaseRequest {
    public String longitude;
    public String latitude;

    @Override
    public String getAPIName() {
        return "fhwl//merchant/getMerchantList";
    }

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return super.appendParams(builder.append("longitude", longitude)
                .append("latitude", latitude));
    }

    @Override
    public boolean usePost() {
        return false;
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<List<MerchantBean>>() {
        };
    }
}
