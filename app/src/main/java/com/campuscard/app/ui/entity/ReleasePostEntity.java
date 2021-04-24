package com.campuscard.app.ui.entity;

import java.util.List;

/**
 * 发布参数提交实体类
 */
public class ReleasePostEntity {

    private String address;
    private String contactPhoneNo;
    private String content;
    private String lostFoundThingsTypeInfoId;
    private String lostFoundType;
    private List<PicturesBean> pictures;


    //编辑id;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPhoneNo() {
        return contactPhoneNo;
    }

    public void setContactPhoneNo(String contactPhoneNo) {
        this.contactPhoneNo = contactPhoneNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLostFoundThingsTypeInfoId() {
        return lostFoundThingsTypeInfoId;
    }

    public void setLostFoundThingsTypeInfoId(String lostFoundThingsTypeInfoId) {
        this.lostFoundThingsTypeInfoId = lostFoundThingsTypeInfoId;
    }

    public String getLostFoundType() {
        return lostFoundType;
    }

    public void setLostFoundType(String lostFoundType) {
        this.lostFoundType = lostFoundType;
    }

    public List<PicturesBean> getPictures() {
        return pictures;
    }

    public void setPictures(List<PicturesBean> pictures) {
        this.pictures = pictures;
    }

    public static class PicturesBean {

        private String imgURL;
        private String sortNo;

        public String getImgURL() {
            return imgURL;
        }

        public void setImgURL(String imgURL) {
            this.imgURL = imgURL;
        }

        public String getSortNo() {
            return sortNo;
        }

        public void setSortNo(String sortNo) {
            this.sortNo = sortNo;
        }
    }
}
