package com.feitianzhu.huangliwo.shop.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;

import java.io.File;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.shop.request
 * user: yangqinbo
 * date: 2020/5/26
 * time: 16:48
 * email: 694125155@qq.com
 */
public class UpLoadFilesRequest extends BaseRequest {
    public List<File> fileList;

    @Override
    public String getAPIName() {
        return "fhwl/files/upload";
    }

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return builder.append("files", fileList).append("filedir", "order/refund/");
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<Object>() {
        };
    }
}
