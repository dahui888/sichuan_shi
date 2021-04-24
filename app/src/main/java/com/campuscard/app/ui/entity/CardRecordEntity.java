package com.campuscard.app.ui.entity;

import java.util.List;

/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 校园充值记录实体类
 */
public class CardRecordEntity {

    private String groupBy;
    private List<ECardRechargeRecordDTOSBean> eCardRechargeRecordDTOS;

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public List<ECardRechargeRecordDTOSBean> getECardRechargeRecordDTOS() {
        return eCardRechargeRecordDTOS;
    }

    public void setECardRechargeRecordDTOS(List<ECardRechargeRecordDTOSBean> eCardRechargeRecordDTOS) {
        this.eCardRechargeRecordDTOS = eCardRechargeRecordDTOS;
    }

    public static class ECardRechargeRecordDTOSBean {

        private String eCardId;
        private String endUserName;
        private double amount;
        private String payMethod;
        private String result;
        private String modifyDate;
        private String groupBy;
        private String id;

        public String getECardId() {
            return eCardId;
        }

        public void setECardId(String eCardId) {
            this.eCardId = eCardId;
        }

        public String getEndUserName() {
            return endUserName;
        }

        public void setEndUserName(String endUserName) {
            this.endUserName = endUserName;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getPayMethod() {
            return payMethod;
        }

        public void setPayMethod(String payMethod) {
            this.payMethod = payMethod;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getModifyDate() {
            return modifyDate;
        }

        public void setModifyDate(String modifyDate) {
            this.modifyDate = modifyDate;
        }

        public String getGroupBy() {
            return groupBy;
        }

        public void setGroupBy(String groupBy) {
            this.groupBy = groupBy;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String geteCardId() {
            return eCardId;
        }

        public void seteCardId(String eCardId) {
            this.eCardId = eCardId;
        }
    }
}
