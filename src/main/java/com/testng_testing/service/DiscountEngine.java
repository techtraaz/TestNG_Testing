package com.testng_testing.service;

import com.testng_testing.model.*;

import org.springframework.stereotype.Service;

@Service
public class DiscountEngine {

    public OrderSummary calculate(Order order) {

        double discount = 0;

        // Rule 1: High value order
        if (order.getAmount() > 5000) {
            discount += order.getAmount() * 0.10;
        }

        // Rule 2: Premium customer
        if (order.getCustomerType() == CustomerType.PREMIUM) {
            discount += order.getAmount() * 0.05;
        }

        // Rule 3: First order bonus
        if (order.isFirstOrder()) {
            discount += 200;
        }

        // Rule 4: Loyalty bonus
        if (order.getLoyaltyPoints() > 1000) {
            discount += 300;
        }

        // Rule 5: Max cap
        if (discount > 2000) {
            discount = 2000;
        }

        double finalAmount = order.getAmount() - discount;

        return new OrderSummary(discount, finalAmount);
    }
}