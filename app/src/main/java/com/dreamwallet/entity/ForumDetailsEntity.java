package com.dreamwallet.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 论坛帖子详情
 */
public class ForumDetailsEntity {

    /**
     * commentList : [{"name":"15972132722","content":"123123"},{"name":"15272132722","content":"123123"},{"name":"15973132722","content":"123123"},{"name":"15972132712","content":"123123"},{"name":"15972132723","content":"123123"}]
     * postInfo : {"imgUrl":"111111","cTime":1503978184000,"title":"测试11","content":"111111"}
     */

    private PostInfoBean postInfo;
    private List<CommentBean> commentList;

    public PostInfoBean getPostInfo() {
        return postInfo;
    }

    public void setPostInfo(PostInfoBean postInfo) {
        this.postInfo = postInfo;
    }

    public List<CommentBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentBean> commentList) {
        this.commentList = commentList;
    }

    public static class PostInfoBean implements Serializable {

//        {
//            cTime = 1505025188000;
//            content = "我";
//            id = 46;
//            pName = "187****1035";
//            pid = 666666701;
//            title = "啊";
//        };

        private String pName;
        private String imgUrl;
        private int pid;
        private int id;
        private long cTime;
        private String title;
        private String content;

        public String getPName() {
            return pName;
        }

        public void setPName(String pName) {
            this.pName = pName;
        }

        public int getPid() {
            return pid;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getCTime() {
            return cTime;
        }

        public void setCTime(long cTime) {
            this.cTime = cTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}
