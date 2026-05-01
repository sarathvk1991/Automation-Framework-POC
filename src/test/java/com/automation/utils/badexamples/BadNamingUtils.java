package com.automation.utils.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// NOT executed at runtime. For SonarQube / custom-QA analysis only.
// =============================================================================
public class BadNamingUtils {

    public String concatenateValues(String firstInput, String secondInput, String thirdInput) {
        return firstInput + secondInput + thirdInput;
    }

    public boolean compareStringLengths(String firstValue, String secondValue) {
        try {
            return firstValue.length() > secondValue.length();
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean areStringsDifferent(String firstString, String secondString) {
        try {
            return !firstString.equals(secondString);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public void printValue(String value) {
        System.out.println(value);
    }

    public String joinWithDash(String firstPart, String secondPart) {
        return firstPart + "-" + secondPart;
    }

    public void checkoutNow(String firstInput, String secondInput, String thirdInput) {
        System.out.println(firstInput + secondInput + thirdInput);
    }

    public void run01(String input, String value) {
        concatenateValues(input, value, input);
        compareStringLengths(input, value);
        areStringsDifferent(input, value);
        printValue(input);
        joinWithDash(input, value);
        checkoutNow(input, value, input);
    }

    public void run02(String input, String value) {
        compareStringLengths(input, value);
        areStringsDifferent(input, value);
        printValue(input);
    }

    public void run03(String input, String value) {
        concatenateValues(input, value, input);
        joinWithDash(input, value);
        checkoutNow(input, value, input);
    }
}
