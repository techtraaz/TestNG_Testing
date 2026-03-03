package com.testng_testing.model;


public class Order {

    private double amount;
    private CustomerType customerType;
    private boolean firstOrder;
    private int loyaltyPoints;

    public Order(double amount, CustomerType customerType,
                 boolean firstOrder, int loyaltyPoints) {

        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        this.amount = amount;
        this.customerType = customerType;
        this.firstOrder = firstOrder;
        this.loyaltyPoints = loyaltyPoints;
    }

    public double getAmount() {
        return amount;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public boolean isFirstOrder() {
        return firstOrder;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }
}
