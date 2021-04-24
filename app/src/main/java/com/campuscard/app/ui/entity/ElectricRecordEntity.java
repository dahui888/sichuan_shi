package com.campuscard.app.ui.entity;

import java.util.List;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 校园电费充值记录实体类
 */
public class ElectricRecordEntity {

    private String groupBy;
    private List<ElectricityRechargeRecordDTOSBean> electricityRechargeRecordDTOS;

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public List<ElectricityRechargeRecordDTOSBean> getElectricRechargeRecordDTOS() {
        return electricityRechargeRecordDTOS;
    }

    public void setElectricRechargeRecordDTOS(List<ElectricityRechargeRecordDTOSBean> electricityRechargeRecordDTOS) {
        this.electricityRechargeRecordDTOS = electricityRechargeRecordDTOS;
    }

    public static class ElectricityRechargeRecordDTOSBean {

        private double amount;
        private String building;
        private String campusName;
        private String groupBy;
        private String id;
        private String modifyDate;
        private String payMethod;
        private String result;
        private String roomNo;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getBuilding() {
            return building;
        }

        public void setBuilding(String building) {
            this.building = building;
        }

        public String getCampusName() {
            return campusName;
        }

        public void setCampusName(String campusName) {
            this.campusName = campusName;
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

        public String getModifyDate() {
            return modifyDate;
        }

        public void setModifyDate(String modifyDate) {
            this.modifyDate = modifyDate;
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

        public String getRoomNo() {
            return roomNo;
        }

        public void setRoomNo(String roomNo) {
            this.roomNo = roomNo;
        }
    }
}
