package com.campuscard.app.ui.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LostFoundEntity implements Serializable {

    private String address;
    private String contactPhoneNo;
    private String content;
    private String createDate;
    private String endUserId;
    private String headPortrait;
    private String id;
    private String lostFoundType;
    private String userName;
    private String viewCount;
    private List<LostAndFoundPictureVOSBean> lostAndFoundPictureVOS;
    private String lostFoundThingsTypeInfoId;
    private String lostFoundThingsTypeName;



    //自定义
    private boolean isSelect;//选中
    private boolean isShowCheck;//显示编辑按钮

    public String getLostFoundThingType() {
        return lostFoundThingsTypeName;
    }

    public void setLostFoundThingType(String lostFoundThingType) {
        this.lostFoundThingsTypeName = lostFoundThingType;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isShowCheck() {
        return isShowCheck;
    }

    public void setShowCheck(boolean showCheck) {
        isShowCheck = showCheck;
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getEndUserId() {
        return endUserId;
    }

    public void setEndUserId(String endUserId) {
        this.endUserId = endUserId;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLostFoundType() {
        return lostFoundType;
    }

    public void setLostFoundType(String lostFoundType) {
        this.lostFoundType = lostFoundType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public List<LostAndFoundPictureVOSBean> getLostAndFoundPictureVOS() {
        return lostAndFoundPictureVOS;
    }

    public void setLostAndFoundPictureVOS(List<LostAndFoundPictureVOSBean> lostAndFoundPictureVOS) {
        this.lostAndFoundPictureVOS = lostAndFoundPictureVOS;
    }


    public String getLostFoundThingsTypeInfoId() {
        return lostFoundThingsTypeInfoId;
    }

    public void setLostFoundThingsTypeInfoId(String lostFoundThingsTypeInfoId) {
        this.lostFoundThingsTypeInfoId = lostFoundThingsTypeInfoId;
    }
}
