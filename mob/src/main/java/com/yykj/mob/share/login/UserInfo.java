package com.yykj.mob.share.login;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.themes.classic.PRTHeader;

public class UserInfo {

    private String userIcon;
    private String userName;
    private Gender userGender;
    private String userId;
    private String platform;//平台

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Gender getUserGender() {
        return userGender;
    }

    public void setUserGender(Gender userGender) {
        this.userGender = userGender;
    }


    public static enum Gender {MALE, FEMALE}


}
