package com.testng_testing;

import com.testng_testing.model.CustomerType;
import com.testng_testing.model.Order;
import com.testng_testing.model.OrderSummary;
import com.testng_testing.service.DiscountEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@SpringBootTest
public class BddStylePresentationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private DiscountEngine engine;

    @Test(
        groups = "regression",
        description = "Demonstrates BDD Style Test Naming (Given-When-Then)"
    )
    public void givenExtremelyHighValueOrder_whenDiscountIsCalculated_thenDiscountShouldNotExceedMaxCap() {
        // GIVEN: An extremely high value order that would calculate to > 2000 in discounts
        // 50,000 * 10% = 5,000 (which is way above the 2000 cap limit)
        Order massiveOrder = new Order(50000, CustomerType.REGULAR, false, 0);

        // WHEN: We calculate the order summary
        OrderSummary summary = engine.calculate(massiveOrder);

        // THEN: The discount applied should be strictly capped at 2000
        Assert.assertEquals(summary.getDiscount(), 5000.0, "Discount should be capped at the maximum allowed value.");
        Assert.assertEquals(summary.getFinalAmount(), 48000.0, "Final amount should correctly reflect the capped discount.");
    }

    @Test(
        groups = "regression",
        description = "Demonstrates BDD Style Test Naming for a standard order with no discounts"
    )
    public void givenRegularCustomerWithSmallOrder_whenDiscountIsCalculated_thenNoDiscountIsApplied() {
        // GIVEN: A standard order from a regular customer that doesn't qualify for any special rules
        Order regularOrder = new Order(1000, CustomerType.REGULAR, false, 0);

        // WHEN: We calculate the order summary
        OrderSummary summary = engine.calculate(regularOrder);

        // THEN: No discount should be given, and final amount should equal the original amount
        Assert.assertEquals(summary.getDiscount(), 0.0, "Regular small order should not get a discount.");
        Assert.assertEquals(summary.getFinalAmount(), 1000.0, "Final amount should be exactly the same as the base amount.");
    }

    @Test(
        groups = "regression",
        description = "Demonstrates BDD Style Test Naming for a Premium customer perk"
    )
    public void givenPremiumCustomer_whenDiscountIsCalculated_thenFlatPremiumDiscountIsApplied() {
        // GIVEN: A premium customer placing a standard-sized order (2000)
        Order premiumOrder = new Order(2000, CustomerType.PREMIUM, false, 0);

        // WHEN: We calculate the order summary
        OrderSummary summary = engine.calculate(premiumOrder);

        // THEN: A 5% discount (2000 * 0.05 = 100) should be applied
        Assert.assertEquals(summary.getDiscount(), 100.0, "Premium customers should get a 5% discount.");
        Assert.assertEquals(summary.getFinalAmount(), 1900.0, "Final amount should correctly reflect the premium discount.");
    }
}
