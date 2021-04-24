package com.campuscard.app.ui.entity;

import java.io.Serializable;

public class LostAndFoundPictureVOSBean implements Serializable {

    private String imgURL;
    private int sortNo;

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public int getSortNo() {
        return sortNo;
    }

    public void setSortNo(int sortNo) {
        this.sortNo = sortNo;
    }
}