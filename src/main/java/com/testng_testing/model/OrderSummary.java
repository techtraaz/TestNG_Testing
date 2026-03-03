package com.testng_testing.model;


public class OrderSummary {

    private double discount;
    private double finalAmount;

    public OrderSummary(double discount, double finalAmount) {
        this.discount = discount;
        this.finalAmount = finalAmount;
    }

    public double getDiscount() {
        return discount;
    }

    public double getFinalAmount() {
        return finalAmount;
    }
}