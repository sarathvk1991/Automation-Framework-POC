package com.automation.utils.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// NOT executed at runtime. For SonarQube / custom-QA analysis only.
//
// SonarQube Issues Demonstrated:
//   [S4]  Generic Exception caught throughout (process, abc, click1, CheckoutNow)
//   [S5]  Empty / swallowed catch blocks
//   [S7]  Non-descriptive method names: doIt, abc, click1, testThing, process,
//         CheckoutNow — all fail naming convention rules
//   [S8]  Non-descriptive parameter names: x, y, tmp
//   [S10] 60 near-identical runner methods — normalised body triggers
//         customqa:duplicate-utility-method on every pair
//
// Metric impact intended:
//   • Naming-compliance ratio  → well below 50 %
//   • Exception-handling score → well below 50 %
// =============================================================================
public class BadNamingUtils {

    // ── Core poorly-named methods ─────────────────────────────────────────────

    // [S7] "doIt" communicates nothing about intent
    // [S8] x, y, tmp are meaningless parameter names
    public String doIt(String x, String y, String tmp) {
        return x + y + tmp;
    }

    // [S7] "process" — process what?
    // [S8] x, y, tmp
    // [S4][S5] catch (Exception e) swallows everything
    public boolean process(String x, String y, String tmp) {
        try {
            return x.length() > y.length();
        } catch (Exception e) {
            return false;
        }
    }

    // [S7] "abc" is gibberish
    // [S8] x, y
    // [S4][S5] catch (Exception e)
    public boolean abc(String x, String y) {
        try {
            return !x.equals(y);
        } catch (Exception e) {
            return false;
        }
    }

    // [S7] "click1" — click what? why 1?
    // [S8] x
    // [S4][S5] catch (Exception e) swallows all errors
    public void click1(String x) {
        try {
            System.out.println(x);
        } catch (Exception e) {
            // swallowed
        }
    }

    // [S7] "testThing" — not a production-quality name
    // [S8] x, y
    public String testThing(String x, String y) {
        return x + "-" + y;
    }

    // [S7] "CheckoutNow" violates Java method-naming conventions (capital C)
    // [S8] x, y, tmp
    // [S4][S5] catch (Exception e)
    public void CheckoutNow(String x, String y, String tmp) {
        try {
            System.out.println(x + y + tmp);
        } catch (Exception e) {
            // swallowed
        }
    }

    // ── Runner methods ────────────────────────────────────────────────────────
    // Each one-liner calls all 6 poorly-named methods → 6 poor-naming hits each.
    // All 3 bodies are identical so customqa:duplicate-utility-method fires on
    // every pair (intentional [S10] demo).
    // ─────────────────────────────────────────────────────────────────────────
    public void run01(String input, String value) { doIt(input, value, input); process(input, value, input); abc(input, value); click1(input); testThing(input, value); CheckoutNow(input, value, input); }
    public void run02(String input, String value) { doIt(input, value, input); process(input, value, input); abc(input, value); click1(input); testThing(input, value); CheckoutNow(input, value, input); }
    public void run03(String input, String value) { doIt(input, value, input); process(input, value, input); abc(input, value); click1(input); testThing(input, value); CheckoutNow(input, value, input); }
}
