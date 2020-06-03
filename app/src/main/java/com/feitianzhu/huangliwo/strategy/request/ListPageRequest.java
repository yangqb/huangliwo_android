package com.feitianzhu.huangliwo.strategy.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.BaseTravelRequest;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.strategy.bean.ListPageBean;
import com.feitianzhu.huangliwo.utils.StringUtils;

import java.util.List;

import static com.feitianzhu.huangliwo.utils.Urls.TICKET_BASE_URL;

/**
 * Created by bch on 2020/5/29
 */
public class ListPageRequest extends BaseTravelRequest {
    //    当前页码
    public int currentPage;
    //    每页显示条数
    public int pageSize;
    //    栏目id 1-会员须知 2-正品保障
    public int columnId;

    @Override
    public Object handleRsponseAfterTransform(Object rsp) {
        if (rsp instanceof ListPageBean) {
            List<ListPageBean.ListBean> list = ((ListPageBean) rsp).getList();
            if (list != null && list.size() >= 0) {
                for (int i = list.size() - 1; i >= 0; i--) {
                    ListPageBean.ListBean listBean = list.get(i);
                    if (listBean.getContentType().equals("1") && StringUtils.isEmpty(listBean.getVideo())) {
                        list.remove(i);
                    }
                }
            }
        }
        return super.handleRsponseAfterTransform(rsp);
    }
    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return super.appendParams(builder
                .append("currentPage", currentPage)
                .append("pageSize", pageSize)
                .append("columnId", columnId)
        );
    }

    @Override
    public boolean usePost() {
        return false;
    }

    @Override
    public String getAPIName() {
        return "title/listPage";
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<ListPageBean>() {
        };
    }

}
