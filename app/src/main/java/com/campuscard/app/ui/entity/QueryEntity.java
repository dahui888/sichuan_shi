package com.campuscard.app.ui.entity;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 房间电费详情
 */
public class QueryEntity {
    private String areaname;
    private String buildingname;
    private String levelname;
    private String roomname;
    private String unitname;
    //剩余电费
    private double sydf;
    //剩余电量
    private double sydl;


    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getBuildingname() {
        return buildingname;
    }

    public void setBuildingname(String buildingname) {
        this.buildingname = buildingname;
    }

    public String getLevelname() {
        return levelname;
    }

    public void setLevelname(String levelname) {
        this.levelname = levelname;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public double getSydf() {
        return sydf;
    }

    public void setSydf(double sydf) {
        this.sydf = sydf;
    }

    public double getSydl() {
        return sydl;
    }

    public void setSydl(double sydl) {
        this.sydl = sydl;
    }
}
