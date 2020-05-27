package com.feitianzhu.huangliwo.shop.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.shop.model.FileModel;

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
    public String filedir;

    @Override
    public String getAPIName() {
        return "fhwl/files/upload";
    }

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        for (File file : fileList
        ) {
            builder.append("files", file);
        }
        return builder.append("filedir", filedir);
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<FileModel>() {
        };
    }
}
