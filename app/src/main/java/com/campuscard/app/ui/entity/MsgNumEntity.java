package com.campuscard.app.ui.entity;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 消息数量
 */
public class MsgNumEntity {
    /**
     * personal : 0
     * pickup_notice : 0
     * system : 0
     */

    private int personal;
    private int pickup_notice;
    private int system;

    public int getPersonal() {
        return personal;
    }

    public void setPersonal(int personal) {
        this.personal = personal;
    }

    public int getPickup_notice() {
        return pickup_notice;
    }

    public void setPickup_notice(int pickup_notice) {
        this.pickup_notice = pickup_notice;
    }

    public int getSystem() {
        return system;
    }

    public void setSystem(int system) {
        this.system = system;
    }



}
