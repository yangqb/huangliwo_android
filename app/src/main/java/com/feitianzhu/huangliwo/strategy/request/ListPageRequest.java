package com.feitianzhu.huangliwo.strategy.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.strategy.bean.ListPageBean;

import static com.feitianzhu.huangliwo.utils.Urls.TICKET_BASE_URL;

/**
 * Created by bch on 2020/5/29
 */
public class ListPageRequest extends BaseRequest {
    //    当前页码
    public int currentPage;
    //    每页显示条数
    public int pageSize;
    //    栏目id 1-会员须知 2-正品保障
    public int columnId;

    @Override
    public String getAPIBaseURL() {
        return TICKET_BASE_URL;
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
        return new TypeReference<Result>() {
        };
    }

    public static class Result {
        /**
         * pageResult : {"totalPage":2,"total":6,"rows":[{"id":2,"columnId":null,"title":"八卦猎奇","images":null,"video":null,"contentType":"2","context":null,"createTime":null,"updateTime":"2020-05-28T16:00:00.000+0000","operator":null,"status":null,"orders":null,"h5Url":"wwww"},{"id":3,"columnId":null,"title":"制造悬念颠倒常识","images":null,"video":null,"contentType":"3","context":null,"createTime":null,"updateTime":"2020-05-28T16:00:00.000+0000","operator":null,"status":null,"orders":null,"h5Url":"wwww"},{"id":18,"columnId":null,"title":"的好好","images":"http://bldby-dev.oss-cn-beijing.aliyuncs.com/titleImages/1590731482597.jpg?Expires=1906091478&OSSAccessKeyId=LTAI4GHEkb4SUqKsjoLMcw1d&Signature=20hD%2B77wSUVa8ob1HRgXm8KZE7Q%3D","video":"http://bldby-dev.oss-cn-beijing.aliyuncs.com/titleVideo/1590731492277.mp4?Expires=1906091484&OSSAccessKeyId=LTAI4GHEkb4SUqKsjoLMcw1d&Signature=pvuGrkh0h%2BDxDbewSC95A0NiSh8%3D","contentType":"2","context":null,"createTime":null,"updateTime":"2020-05-28T16:00:00.000+0000","operator":null,"status":null,"orders":null,"h5Url":"wwww"},{"id":4,"columnId":null,"title":"颠倒常识","images":null,"video":null,"contentType":"1","context":null,"createTime":null,"updateTime":"2020-05-28T16:00:00.000+0000","operator":null,"status":null,"orders":null,"h5Url":"wwww"},{"id":20,"columnId":null,"title":"大撒大撒大苏打","images":"http://bldby-dev.oss-cn-beijing.aliyuncs.com/titleImages/1590731722577.jpg?Expires=1906091720&OSSAccessKeyId=LTAI4GHEkb4SUqKsjoLMcw1d&Signature=FFyFNQGe%2BlSC%2BKgz9TYGPlhsHRg%3D","video":"","contentType":"2","context":null,"createTime":null,"updateTime":"2020-05-28T16:00:00.000+0000","operator":null,"status":null,"orders":null,"h5Url":"wwww"}]}
         */

        public ListPageBean pageResult;


    }
}
