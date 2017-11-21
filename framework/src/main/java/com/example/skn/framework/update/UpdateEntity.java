package com.example.skn.framework.update;

/**
 * Created by hf
 */
public class UpdateEntity {


    /**
     * code : 000
     * mesg : 操作成功!
     * data : {"appVersion":"1.0","appType":true,"needUpdate":false,"apkDownPath":"","updateTime":1504865417000,"id":1}
     */

    private String code;
    private String mesg;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMesg() {
        return mesg;
    }

    public void setMesg(String mesg) {
        this.mesg = mesg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * appVersion : 1.0
         * appType : true
         * needUpdate : false
         * apkDownPath :
         * updateTime : 1504865417000
         * id : 1
         */

        private String appVersion;
        private boolean appType;
        private boolean needUpdate;
        private String apkDownPath;
        private String updateContent;
        private long updateTime;
        private long size;
        private String file_MD5;
        private int id;

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public boolean isAppType() {
            return appType;
        }

        public void setAppType(boolean appType) {
            this.appType = appType;
        }

        public boolean isNeedUpdate() {
            return needUpdate;
        }

        public void setNeedUpdate(boolean needUpdate) {
            this.needUpdate = needUpdate;
        }

        public String getApkDownPath() {
            return apkDownPath;
        }

        public void setApkDownPath(String apkDownPath) {
            this.apkDownPath = apkDownPath;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public String getUpdateContent() {
            return updateContent;
        }

        public void setUpdateContent(String updateContent) {
            this.updateContent = updateContent;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getFile_MD5() {
            return file_MD5;
        }

        public void setFile_MD5(String file_MD5) {
            this.file_MD5 = file_MD5;
        }
    }
}
