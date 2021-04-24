package com.campuscard.app.ui.entity;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 消息列表数据
 */
public class MsgEntity {

    private String content;
    private String id;
    private String imgURL;
    private String messageReadStatus;
    private String sendDate;
    private String title;
    private String createAdminUserId;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getMessageReadStatus() {
        return messageReadStatus;
    }

    public void setMessageReadStatus(String messageReadStatus) {
        this.messageReadStatus = messageReadStatus;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getCreateAdminUserId() {
        return createAdminUserId;
    }

    public void setCreateAdminUserId(String createAdminUserId) {
        this.createAdminUserId = createAdminUserId;
    }
}
