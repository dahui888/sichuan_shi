package com.campuscard.app.ui.entity;

import java.util.List;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 圈存记录
 */
public class TranerEntity {


    private double totalMoney;
    private List<RechargeRecsBean> rechargeRecs;

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public List<RechargeRecsBean> getRechargeRecs() {
        return rechargeRecs;
    }

    public void setRechargeRecs(List<RechargeRecsBean> rechargeRecs) {
        this.rechargeRecs = rechargeRecs;
    }

}
