
# TestNG Testing Framework - Comprehensive Guide

## 📋 Introduction

TestNG is a powerful testing framework inspired by JUnit and NUnit, designed to make testing more flexible and comprehensive. It covers all categories of tests including unit, functional, end-to-end, integration, and more. This repository demonstrates best practices in test automation using TestNG with Spring Boot.

## 🎯 Overview of Test Implementation

### Test Class: DiscountEngineTest

This test class extends `AbstractTestNGSpringContextTests` to leverage Spring Boot's dependency injection capabilities. It tests the `DiscountEngine` business logic which applies various discount rules:

- **10% discount** for high-value orders
- **5% extra discount** for premium customers
- **200 fixed discount** for first-time orders
- **300 loyalty points** for customers with over 1000 points
- **Maximum discount cap** of 2000

## ✅ Testing Features Demonstrated

### 1. **Assertions & Validations**
- Uses `Assert.assertEquals()` for exact value matching
- Implements `SoftAssert` for multiple assertions without stopping at first failure
- Validates discount amounts, final prices, and business rule compliance

```java
SoftAssert softAssert = new SoftAssert();
softAssert.assertTrue(discount > 0, "Discount should be positive");
softAssert.assertTrue(discount <= MAX_DISCOUNT, "Discount should not exceed cap");
softAssert.assertEquals(finalAmount, expectedAmount, "Final amount mismatch");
softAssert.assertAll();
```

### 2. **Fixtures & Setup/Teardown**
- `@BeforeClass setupSuite()` - Initializes test fixtures once before all tests
- `@AfterClass cleanup()` - Cleans up resources after all tests
- Proper resource management for test lifecycle

### 3. **Data-Driven Testing with @DataProvider**
- Uses `@DataProvider("customerTypes")` to provide multiple input combinations
- Single test method executes with different customer types without code duplication
- Supports parameterized testing for comprehensive coverage

```java
@DataProvider(name = "customerTypes")
public Object[][] getCustomerTypes() {
    return new Object[][] {
        { "REGULAR", 5000, false },
        { "PREMIUM", 10000, true },
        { "VIP", 20000, true }
    };
}
```

### 4. **BDD Style Test Naming**
- Test method names follow Given-When-Then pattern
- Examples:
  - `givenHighValueOrder_whenCalculate_thenApplyDiscount()`
  - `givenPremiumCustomer_whenCalculate_thenApplyExtraDiscount()`
  - `givenFirstOrder_whenCalculate_thenApplyFirstOrderDiscount()`
- Makes test intent clear and readable for non-developers

### 5. **Test Reporting & Listeners**
- Implements `CustomTestListener` with `ITestListener` interface
- `onTestSuccess()` - Logs passed tests
- `onTestFailure()` - Captures failed test details
- Automatic HTML report generation in `test-output/index.html`

### 6. **Test Grouping**
- Organizes tests into logical groups:
  - `@Test(groups = "smoke")` - Quick validation tests
  - `@Test(groups = "regression")` - Full verification tests
- Allows selective test execution based on requirements

```xml
<!-- In testng.xml -->
<groups>
    <run>
        <include name="smoke"/>
        <include name="regression"/>
    </run>
</groups>
```

## 🛠️ Project Structure

```
TestNG_Testing/
├── pom.xml                          # Maven configuration
├── testng.xml                       # TestNG suite configuration
├── src/
│   ├── main/java/
│   │   └── com/testng_testing/
│   │       ├── DiscountEngine.java  # Business logic
│   │       └── Order.java           # Model class
│   └── test/java/
│       └── com/testng_testing/
│           ├── DiscountEngineTest.java      # Main test class
│           └── listener/
│               └── CustomTestListener.java   # Test event listener
├── test-output/                     # Generated HTML reports
└── README.md                        # This file
```

## 📦 Dependencies

The project uses:
- **Spring Boot 3.2.5** - Application framework
- **TestNG 7.9.0** - Testing framework
- **Mockito** - Mocking framework (optional)
- **Java 21** - Programming language

## 🚀 Running Tests

### 1. Clean and Run All Tests
```bash
mvn clean test
```

This command:
- Cleans previous build artifacts
- Compiles source code
- Runs all tests in `testng.xml`

### 2. Run Only Smoke Tests
```bash
mvn clean test -Dgroups=smoke
```

### 3. Run Only Regression Tests
```bash
mvn clean test -Dgroups=regression
```

### 4. Run Specific Test Class
```bash
mvn clean test -Dtest=DiscountEngineTest
```

### 5. Run Tests with Maven Wrapper (No Maven Installation Required)
**On Linux/Mac:**
```bash
./mvnw clean test
```

**On Windows:**
```bash
mvnw.cmd clean test
```

### 6. Skip Tests During Build
```bash
mvn clean install -DskipTests
```

## 📊 Viewing Test Reports

### Step 1: Locate the Report Directory
After running tests, navigate to the `test-output` directory in your project:
```
project-root/test-output/
```

### Step 2: Open the HTML Report
1. Look for `index.html` file in the `test-output` directory
2. Open it with any web browser:
   - **Right-click** → Open with → Choose your browser
   - Or **drag and drop** the file into your browser
   - Or use command line:
     ```bash
     # On Mac
     open test-output/index.html
     
     # On Linux
     xdg-open test-output/index.html
     
     # On Windows
     start test-output/index.html
     ```

### Step 3: Report Contents
The HTML report displays:
- **Test Summary**: Total tests, passed, failed, skipped counts
- **Test Details**: Each test method with execution time
- **Stack Traces**: Complete error details for failed tests
- **Groups**: Tests organized by their assigned groups
- **Graphs**: Visual representation of test results
- **Timeline**: Test execution duration

## 📝 Sample Test Case Breakdown

```java
@Test(groups = "smoke")
public void givenHighValueOrder_whenCalculate_thenApplyDiscount() {
    // GIVEN - Setup test data
    Order order = new Order();
    order.setAmount(15000);
    order.setCustomerType("REGULAR");
    
    // WHEN - Execute business logic
    double discount = discountEngine.calculate(order);
    
    // THEN - Assert expected results
    Assert.assertEquals(discount, 1500.0); // 10% of 15000
    Assert.assertTrue(discount <= MAX_DISCOUNT);
}
```

## 🔧 Configuration

### pom.xml Key Settings
```xml
<!-- Maven Surefire Plugin for Test Execution -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.2.5</version>
    <configuration>
        <suiteXmlFiles>
            <suiteXmlFile>testng.xml</suiteXmlFile>
        </suiteXmlFiles>
    </configuration>
</plugin>
```

### testng.xml Suite Configuration
```xml
<suite name="DiscountEngineSuite">
    <listeners>
        <listener class-name="com.testng_testing.listener.CustomTestListener"/>
    </listeners>
    
    <test name="BusinessLogicTests">
        <groups>
            <run>
                <include name="smoke"/>
                <include name="regression"/>
            </run>
        </groups>
        <classes>
            <class name="com.testng_testing.DiscountEngineTest"/>
        </classes>
    </test>
</suite>
```

## 💡 Best Practices

1. **Use Meaningful Test Names** - Follow BDD pattern (Given-When-Then)
2. **Separate Concerns** - Keep setup, execution, and assertion distinct
3. **Use Data Providers** - Avoid test code duplication with parameterization
4. **Group Tests Logically** - Organize by smoke, regression, integration, etc.
5. **Implement Listeners** - Track test execution and generate custom reports
6. **Clean Up Resources** - Use @AfterClass for proper cleanup
7. **Assert Multiple Conditions** - Use SoftAssert when needed

## 🎓 Learning Resources

- [TestNG Official Documentation](https://testng.org/doc/)
- [Spring Boot Testing Guide](https://spring.io/guides/gs/testing-web/)
- [Maven Surefire Documentation](https://maven.apache.org/surefire/)

## 📞 Support

For issues or questions about this project:
1. Check the test output reports for detailed error messages
2. Review test implementation in `src/test/java`
3. Examine the business logic in `src/main/java`


---

**Happy Testing! 🧪**


