package com.feitianzhu.huangliwo.strategy.bean;

import java.util.List;

/**
 * Created by bch on 2020/5/29
 */
public class ListPageBean {

    /**
     * totalPage : 2
     * total : 6
     * rows : [{"id":2,"columnId":null,"title":"八卦猎奇","images":null,"video":null,"contentType":"2","context":null,"createTime":null,"updateTime":"2020-05-28T16:00:00.000+0000","operator":null,"status":null,"orders":null,"h5Url":"wwww"},{"id":3,"columnId":null,"title":"制造悬念颠倒常识","images":null,"video":null,"contentType":"3","context":null,"createTime":null,"updateTime":"2020-05-28T16:00:00.000+0000","operator":null,"status":null,"orders":null,"h5Url":"wwww"},{"id":18,"columnId":null,"title":"的好好","images":"http://bldby-dev.oss-cn-beijing.aliyuncs.com/titleImages/1590731482597.jpg?Expires=1906091478&OSSAccessKeyId=LTAI4GHEkb4SUqKsjoLMcw1d&Signature=20hD%2B77wSUVa8ob1HRgXm8KZE7Q%3D","video":"http://bldby-dev.oss-cn-beijing.aliyuncs.com/titleVideo/1590731492277.mp4?Expires=1906091484&OSSAccessKeyId=LTAI4GHEkb4SUqKsjoLMcw1d&Signature=pvuGrkh0h%2BDxDbewSC95A0NiSh8%3D","contentType":"2","context":null,"createTime":null,"updateTime":"2020-05-28T16:00:00.000+0000","operator":null,"status":null,"orders":null,"h5Url":"wwww"},{"id":4,"columnId":null,"title":"颠倒常识","images":null,"video":null,"contentType":"1","context":null,"createTime":null,"updateTime":"2020-05-28T16:00:00.000+0000","operator":null,"status":null,"orders":null,"h5Url":"wwww"},{"id":20,"columnId":null,"title":"大撒大撒大苏打","images":"http://bldby-dev.oss-cn-beijing.aliyuncs.com/titleImages/1590731722577.jpg?Expires=1906091720&OSSAccessKeyId=LTAI4GHEkb4SUqKsjoLMcw1d&Signature=FFyFNQGe%2BlSC%2BKgz9TYGPlhsHRg%3D","video":"","contentType":"2","context":null,"createTime":null,"updateTime":"2020-05-28T16:00:00.000+0000","operator":null,"status":null,"orders":null,"h5Url":"wwww"}]
     */

    private int totalPage;
    private int total;
    private List<RowsBean> rows;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * id : 2
         * columnId : null
         * title : 八卦猎奇
         * images : null
         * video : null
         * contentType : 2
         * context : null
         * createTime : null
         * updateTime : 2020-05-28T16:00:00.000+0000
         * operator : null
         * status : null
         * orders : null
         * h5Url : wwww
         */

        private int id;
        private Object columnId;
        private String title;
        private Object images;
        private Object video;
        private String contentType;
        private Object context;
        private Object createTime;
        private String updateTime;
        private Object operator;
        private Object status;
        private Object orders;
        private String h5Url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getColumnId() {
            return columnId;
        }

        public void setColumnId(Object columnId) {
            this.columnId = columnId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getImages() {
            return images;
        }

        public void setImages(Object images) {
            this.images = images;
        }

        public Object getVideo() {
            return video;
        }

        public void setVideo(Object video) {
            this.video = video;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public Object getContext() {
            return context;
        }

        public void setContext(Object context) {
            this.context = context;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public Object getOperator() {
            return operator;
        }

        public void setOperator(Object operator) {
            this.operator = operator;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public Object getOrders() {
            return orders;
        }

        public void setOrders(Object orders) {
            this.orders = orders;
        }

        public String getH5Url() {
            return h5Url;
        }

        public void setH5Url(String h5Url) {
            this.h5Url = h5Url;
        }
    }

}
