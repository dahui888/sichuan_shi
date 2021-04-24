package com.campuscard.app.ui.entity;

/**
 * 这是保存上传图片后返回回来的图片id和url
 */
public class PictureIdEntity {
    /**
     * 这个id就是fileServiceId
     */
    private String id;
    /**
     * 图片的地址
     */
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
