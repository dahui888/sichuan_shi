package com.campuscard.app.ui.entity;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 获取消息类型和未读数量
 */
public class NoReadMsgNumEntity {

    /**
     * messageType : SYSTEM
     * unread_number : 0
     */

    private String messageType;
    private String unread_number;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getUnread_number() {
        return unread_number;
    }

    public void setUnread_number(String unread_number) {
        this.unread_number = unread_number;
    }
}
