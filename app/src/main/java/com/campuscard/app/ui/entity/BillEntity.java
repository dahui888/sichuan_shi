package com.campuscard.app.ui.entity;

import java.io.Serializable;

public class BillEntity implements Serializable {

    private float totalAmount;
    private float mealBill;
    private float shoppingBill;
    private float electricityBill;
    private float other;

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public float getMealBill() {
        return mealBill;
    }

    public void setMealBill(float mealBill) {
        this.mealBill = mealBill;
    }

    public float getShoppingBill() {
        return shoppingBill;
    }

    public void setShoppingBill(float shoppingBill) {
        this.shoppingBill = shoppingBill;
    }

    public float getElectricityBill() {
        return electricityBill;
    }

    public void setElectricityBill(float electricityBill) {
        this.electricityBill = electricityBill;
    }

    public float getOther() {
        return other;
    }

    public void setOther(float other) {
        this.other = other;
    }
}
