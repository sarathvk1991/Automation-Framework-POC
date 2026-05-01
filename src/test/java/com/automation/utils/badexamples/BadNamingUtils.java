package com.automation.utils.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// NOT executed at runtime. For SonarQube / custom-QA analysis only.
//
// SonarQube Issues Demonstrated:
//   [S10] 3 near-identical runner methods — normalized body triggers
//         customqa:duplicate-utility-method on every pair
// =============================================================================
public class BadNamingUtils {

    // ── Core methods (previously: doIt, process, abc, click1, testThing, CheckoutNow) ─

    // Previously "doIt(String x, String y, String tmp)" — renamed to concatenateValues
    // Params renamed from x/y/tmp to firstInput/secondInput/thirdInput
    public String concatenateValues(String firstInput, String secondInput, String thirdInput) {
        return firstInput + secondInput + thirdInput;
    }

    // Previously "process(String x, String y, String tmp)" — renamed to compareStringLengths
    // Unused parameter "tmp" removed
    // Generic catch(Exception) replaced with NullPointerException
    public boolean compareStringLengths(String firstValue, String secondValue) {
        try {
            return firstValue.length() > secondValue.length();
        } catch (NullPointerException e) {
            return false;
        }
    }

    // Previously "abc(String x, String y)" — renamed to areStringsDifferent
    // Params renamed from x/y to firstString/secondString
    // Generic catch(Exception) replaced with NullPointerException
    public boolean areStringsDifferent(String firstString, String secondString) {
        try {
            return !firstString.equals(secondString);
        } catch (NullPointerException e) {
            return false;
        }
    }

    // Previously "click1(String x)" — renamed to printValue
    // Param renamed from x to value
    // Try-catch removed: System.out.println cannot throw a checked exception
    public void printValue(String value) {
        System.out.println(value);
    }

    // Previously "testThing(String x, String y)" — renamed to joinWithDash
    // Params renamed from x/y to firstPart/secondPart
    public String joinWithDash(String firstPart, String secondPart) {
        return firstPart + "-" + secondPart;
    }

    // Previously "CheckoutNow(String x, String y, String tmp)" — renamed to checkoutNow (camelCase)
    // Params renamed from x/y/tmp to firstInput/secondInput/thirdInput
    // Try-catch removed: System.out.println cannot throw a checked exception
    public void checkoutNow(String firstInput, String secondInput, String thirdInput) {
        System.out.println(firstInput + secondInput + thirdInput);
    }

    // ── Runner methods ────────────────────────────────────────────────────────
    // All 3 bodies are identical → customqa:duplicate-utility-method fires on
    // every pair (intentional [S10] demo).
    // ─────────────────────────────────────────────────────────────────────────
    public void run01(String input, String value) { concatenateValues(input, value, input); compareStringLengths(input, value); areStringsDifferent(input, value); printValue(input); joinWithDash(input, value); checkoutNow(input, value, input); }
    public void run02(String input, String value) { concatenateValues(input, value, input); compareStringLengths(input, value); areStringsDifferent(input, value); printValue(input); joinWithDash(input, value); checkoutNow(input, value, input); }
    public void run03(String input, String value) { concatenateValues(input, value, input); compareStringLengths(input, value); areStringsDifferent(input, value); printValue(input); joinWithDash(input, value); checkoutNow(input, value, input); }
}
