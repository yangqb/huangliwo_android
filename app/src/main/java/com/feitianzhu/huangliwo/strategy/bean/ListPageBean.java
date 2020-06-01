package com.feitianzhu.huangliwo.strategy.bean;

import java.util.List;

/**
 * Created by bch on 2020/5/29
 */
public class ListPageBean {


    /**
     * totalPage : 2
     * total : 6
     * list : [{"title":"八卦猎奇","images":null,"video":null,"contentType":"2","updateTime":"2020.05.29","h5Url":"wwww"},{"title":"制造悬念颠倒常识","images":null,"video":null,"contentType":"3","updateTime":"2020.05.29","h5Url":"wwww"},{"title":"的好好","images":"http://bldby-dev.oss-cn-beijing.aliyuncs.com/titleImages/1590731482597.jpg?Expires=1906091478&OSSAccessKeyId=LTAI4GHEkb4SUqKsjoLMcw1d&Signature=20hD%2B77wSUVa8ob1HRgXm8KZE7Q%3D","video":"http://bldby-dev.oss-cn-beijing.aliyuncs.com/titleVideo/1590731492277.mp4?Expires=1906091484&OSSAccessKeyId=LTAI4GHEkb4SUqKsjoLMcw1d&Signature=pvuGrkh0h%2BDxDbewSC95A0NiSh8%3D","contentType":"2","updateTime":"2020.05.29","h5Url":"wwww"},{"title":"颠倒常识","images":null,"video":null,"contentType":"1","updateTime":"2020.05.29","h5Url":"wwww"},{"title":"大撒大撒大苏打","images":"http://bldby-dev.oss-cn-beijing.aliyuncs.com/titleImages/1590731722577.jpg?Expires=1906091720&OSSAccessKeyId=LTAI4GHEkb4SUqKsjoLMcw1d&Signature=FFyFNQGe%2BlSC%2BKgz9TYGPlhsHRg%3D","video":"","contentType":"2","updateTime":"2020.05.29","h5Url":"wwww"}]
     */

    private int totalPage;
    private int total;
    private List<ListBean> list;

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

    public List<ListPageBean.ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * title : 八卦猎奇
         * images : null
         * video : null
         * contentType : 2
         * updateTime : 2020.05.29
         * h5Url : wwww
         */

        private String title;
        private String images;
        private String video;
        private String contentType;
        private String updateTime;
        private String h5Url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getH5Url() {
            return h5Url;
        }

        public void setH5Url(String h5Url) {
            this.h5Url = h5Url;
        }
    }
}
