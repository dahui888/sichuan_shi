package com.campuscard.app.ui.entity;

public class ChartDataEntity {

    private float size;
    private String top;
    private String bottom;
    private String color;


    public ChartDataEntity(float size, String top, String bottom, String color) {
        this.size = size;
        this.top = top;
        this.bottom = bottom;
        this.color = color;
    }


    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getBottom() {
        return bottom;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}