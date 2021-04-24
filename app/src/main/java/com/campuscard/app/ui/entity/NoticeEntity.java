package com.campuscard.app.ui.entity;

import java.util.List;

public class NoticeEntity {


    private String id;
    private String title;
    private String crateDate;
    private int viewCount;
    private String isTop;
    private String imgUrl;
    private List<AnnouncementPicturesBean> announcementPictures;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCrateDate() {
        return crateDate;
    }

    public void setCrateDate(String crateDate) {
        this.crateDate = crateDate;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public List<AnnouncementPicturesBean> getAnnouncementPictures() {
        return announcementPictures;
    }

    public void setAnnouncementPictures(List<AnnouncementPicturesBean> announcementPictures) {
        this.announcementPictures = announcementPictures;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public static class AnnouncementPicturesBean {

        private String imgURL;

        public String getImgURL() {
            return imgURL;
        }

        public void setImgURL(String imgURL) {
            this.imgURL = imgURL;
        }
    }
}
