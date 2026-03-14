package com.testng_testing;

import com.testng_testing.model.CustomerType;
import com.testng_testing.model.Order;
import com.testng_testing.model.OrderSummary;
import com.testng_testing.service.DiscountEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SpringBootTest
public class DataDrivenPresentationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private DiscountEngine engine;

    // Data Provider supplying test scenarios
    @DataProvider(name = "loyaltyAndFirstOrderScenarios")
    public Object[][] provideLoyaltyScenarios() {
        return new Object[][] {
            // isFirstOrder, loyaltyPoints, expectedDiscount
            { true,  0,    200.0 }, // Only gets the first order bonus (+200)
            { false, 1500, 300.0 }, // Only gets the loyalty bonus (+300)
            { true,  1500, 500.0 }, // Gets both bonuses (200 + 300 = 500)
            { false, 500,  0.0   }  // Gets neither bonus
        };
    }

    @Test(
        dataProvider = "loyaltyAndFirstOrderScenarios", 
        groups = "presentation",
        description = "Demonstrates Data-Driven Testing using a DataProvider"
    )
    public void testVariousLoyaltyAndFirstOrderCombos(boolean isFirstOrder, int loyaltyPoints, double expectedDiscount) {
        // Arrange
        double baseAmount = 1000.0; // Keep amount strictly under 5000 to avoid high-value rules
        Order order = new Order(baseAmount, CustomerType.REGULAR, isFirstOrder, loyaltyPoints);
        
        // Act
        OrderSummary summary = engine.calculate(order);

        // Assert
        Assert.assertEquals(summary.getDiscount(), expectedDiscount, "The calculated discount did not match the expected scenario.");
    }
}
