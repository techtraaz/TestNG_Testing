package com.testng_testing.listener;

import org.testng.ITestListener;
import org.testng.ITestResult;

public class CustomTestListener implements ITestListener {

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("PASSED: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("FAILED: " + result.getName());
    }
}