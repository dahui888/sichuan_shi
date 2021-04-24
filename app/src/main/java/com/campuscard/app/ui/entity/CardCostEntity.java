package com.campuscard.app.ui.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 一卡通消费
 */
public class CardCostEntity {

    private String groupBy;
    private List<ConsumeDTOSBean> consumeDTOS;

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public List<ConsumeDTOSBean> getConsumeDTOS() {
        return consumeDTOS;
    }

    public void setConsumeDTOS(List<ConsumeDTOSBean> consumeDTOS) {
        this.consumeDTOS = consumeDTOS;
    }

    public static class ConsumeDTOSBean {

        private String consumeName;
        private double amount;
        private String consumeTypeCode;
        private long time;
        private String date;
        private String place;
        private String groupBy;

        public String getConsumeName() {
            return consumeName;
        }

        public void setConsumeName(String consumeName) {
            this.consumeName = consumeName;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getConsumeTypeCode() {
            return consumeTypeCode;
        }

        public void setConsumeTypeCode(String consumeTypeCode) {
            this.consumeTypeCode = consumeTypeCode;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getGroupBy() {
            return groupBy;
        }

        public void setGroupBy(String groupBy) {
            this.groupBy = groupBy;
        }
    }
}
