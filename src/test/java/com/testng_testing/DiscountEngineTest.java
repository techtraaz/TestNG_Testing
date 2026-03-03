package com.testng_testing;


import com.testng_testing.model.*;
import com.testng_testing.service.DiscountEngine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

@SpringBootTest
public class DiscountEngineTest
        extends AbstractTestNGSpringContextTests {

    @Autowired
    private DiscountEngine engine;

    @BeforeClass
    public void setupSuite() {
        System.out.println("Starting Discount Engine Tests");
    }

    // Data Provider
    @DataProvider(name = "customerTypes")
    public Object[][] customerTypes() {
        return new Object[][]{
                {CustomerType.REGULAR, 0.0},
                {CustomerType.PREMIUM, 250.0}
        };
    }

    @Test(groups = "smoke",
            description = "Given high value order When calculate Then apply 10% discount")
    public void givenHighValueOrder_whenCalculate_thenApplyDiscount() {

        Order order = new Order(6000, CustomerType.REGULAR, false, 0);
        OrderSummary summary = engine.calculate(order);

        Assert.assertEquals(summary.getDiscount(), 600);
        Assert.assertEquals(summary.getFinalAmount(), 5400);
    }

    @Test(dataProvider = "customerTypes", groups = "regression")
    public void givenCustomerType_whenCalculate_thenApplyCorrectDiscount(
            CustomerType type, double expectedExtra) {

        Order order = new Order(5000, type, false, 0);
        OrderSummary summary = engine.calculate(order);

        Assert.assertEquals(summary.getDiscount(), expectedExtra);
    }

    @Test(groups = "regression")
    public void givenPremiumHighValueFirstOrder_whenCalculate_thenApplyAllRules() {

        Order order = new Order(10000, CustomerType.PREMIUM, true, 1500);
        OrderSummary summary = engine.calculate(order);

        SoftAssert soft = new SoftAssert();
        soft.assertTrue(summary.getDiscount() > 0);
        soft.assertTrue(summary.getDiscount() <= 2000);
        soft.assertEquals(summary.getFinalAmount(),
                order.getAmount() - summary.getDiscount());
        soft.assertAll();
    }
}