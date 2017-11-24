package com.dreamwallet.entity;

import java.util.List;

/**
 * Created by DIY on 2017/7/24.
 */

public class HelpCenterBean {


    /**
     * channel : 帮助中心1
     * contentList : [{"detail":"1","title":"帮助中心1"}]
     */

    private String channel;
    private List<ContentListBean> contentList;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public List<ContentListBean> getContentList() {
        return contentList;
    }

    public void setContentList(List<ContentListBean> contentList) {
        this.contentList = contentList;
    }

    public static class ContentListBean {
        /**
         * detail : 1
         * title : 帮助中心1
         */

        private String detail;
        private String title;

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
