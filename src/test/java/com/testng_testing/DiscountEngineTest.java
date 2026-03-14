package com.testng_testing;


import com.testng_testing.model.*;
import com.testng_testing.service.DiscountEngine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

@SpringBootTest
public class DiscountEngineTest
        extends AbstractTestNGSpringContextTests {

    @Autowired
    private DiscountEngine engine;

    private Order testOrder;

    // ========== FIXTURES - SETUP/TEARDOWN MECHANISMS ==========

    @BeforeClass(alwaysRun = true)
    public void setupSuite() {
        System.out.println("\n=== Starting Discount Engine Tests ===");
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeEachTest() {
        System.out.println("  Setting up test data before test");
        testOrder = null;
    }

    @AfterMethod(alwaysRun = true)
    public void afterEachTest(ITestResult result) {
        String status = result.isSuccess() ? "PASSED" : "FAILED";
        System.out.println("  Test " + result.getName() + " - " + status);
        testOrder = null;
    }

    // ========== DATA PROVIDER ==========

    @DataProvider(name = "customerTypes")
    public Object[][] customerTypes() {
        return new Object[][]{
                {CustomerType.REGULAR, 0.0},
                {CustomerType.PREMIUM, 250.0}
        };
    }

    // ========== POSITIVE TEST CASES ==========

    @Test(groups = "smoke",
            description = "Given high value order When calculate Then apply 10% discount")
    public void givenHighValueOrder_whenCalculate_thenApplyDiscount() {

        Order order = new Order(6000, CustomerType.REGULAR, false, 0);
        OrderSummary summary = engine.calculate(order);

        Assert.assertEquals(summary.getDiscount(), 600.0);
        Assert.assertEquals(summary.getFinalAmount(), 5400.0);
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

    // ========== NEGATIVE TEST CASES ==========

    @Test(groups = {"negative", "smoke"},
          description = "GIVEN null order WHEN calculate THEN throw NullPointerException",
          expectedExceptions = NullPointerException.class)
    public void givenNullOrder_whenCalculate_thenThrowException() {
        engine.calculate(null);
    }

    @Test(groups = {"negative", "regression"},
          description = "GIVEN negative amount WHEN create order THEN throw IllegalArgumentException",
          expectedExceptions = IllegalArgumentException.class)
    public void givenNegativeAmount_whenCreateOrder_thenThrowException() {
        testOrder = new Order(-5000, CustomerType.REGULAR, false, 0);
    }
}