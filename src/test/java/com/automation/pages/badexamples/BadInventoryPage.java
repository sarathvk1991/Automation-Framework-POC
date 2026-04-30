package com.automation.pages.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// SonarQube Issues Demonstrated:
//   [S1]  No base class — boilerplate WebDriver code duplicated from BadLoginPage
//   [S2]  Locator strings repeated inline in every method (no constants)
//   [S6]  System.out.println instead of logger
//   [S7]  Non-descriptive method names: getData(), doSort(), click2()
//   [S8]  Non-descriptive variables: x, y, tmp, result
//   [S10] Duplicate method bodies (getNames and fetchNames are identical)
//   [S11] Long method mixing sort + collect + assert in one call
//   [S12] Flaky direct element access without explicit wait throughout
// =============================================================================

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class BadInventoryPage {

    // [S1] Copy-pasted from BadLoginPage — no shared base class
    private WebDriver driver;

    public BadInventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    // ── [S7][S2] Non-descriptive method, repeated locator ─────────────────────

    // [S7] "getData" — data of what? returns what type? completely opaque
    // [S2] ".inventory_list" hardcoded — not a constant
    // [S12] Direct element access without wait
    public boolean getData() {
        WebElement x = driver.findElement(By.cssSelector(".inventory_list")); // [S8][S12]
        return x.isDisplayed();
    }

    // ── [S7] Non-descriptive ── [S2] Repeated locator ─────────────────────────

    // [S7] "click2" gives no indication of what element is being clicked
    // [S2] "[data-test='product-sort-container']" hardcoded, repeated below
    // [S12] Direct element interaction without wait
    public void click2(String x) { // [S8] parameter 'x' is the sort option text
        WebElement tmp = driver.findElement( // [S8] 'tmp' [S12]
            By.cssSelector("[data-test='product-sort-container']")
        );
        // [S11] Mixed concern: finds element, creates Select, AND interacts
        new org.openqa.selenium.support.ui.Select(tmp).selectByVisibleText(x);
    }

    // ── [S7] Opaque method name ─────────────────────────────────────────────

    // [S7] "doSort" — sort by what? ascending or descending?
    // [S2] Locator "[data-test='product-sort-container']" repeated (same as click2)
    // [S12] Direct element access without wait
    public void doSort(String sortText) {
        WebElement y = driver.findElement( // [S8] 'y' [S12]
            By.cssSelector("[data-test='product-sort-container']")
        );
        new org.openqa.selenium.support.ui.Select(y).selectByVisibleText(sortText);
    }

    // ── [S10] Duplicate method bodies ─────────────────────────────────────────

    // [S10] getNames() and fetchNames() below are structurally identical — SonarQube dupe block
    // [S2] ".inventory_item_name" hardcoded
    // [S12] Direct findElements without wait
    public List<String> getNames() {
        List<String> result = new ArrayList<>(); // [S8] variable 'result'
        List<WebElement> x = driver.findElements( // [S8] 'x' [S12]
            By.cssSelector(".inventory_item_name")
        );
        for (WebElement y : x) { // [S8] 'y'
            result.add(y.getText());
        }
        return result;
    }

    // [S10] Identical body to getNames() — duplicate code block
    public List<String> fetchNames() {
        List<String> result = new ArrayList<>();
        List<WebElement> x = driver.findElements( // [S12]
            By.cssSelector(".inventory_item_name")
        );
        for (WebElement y : x) {
            result.add(y.getText());
        }
        return result;
    }

    // ── [S11] Long method mixing multiple responsibilities ─────────────────────

    // [S11] This single method: sorts, collects prices, validates order, logs result.
    //       Four distinct concerns in one method — far too many responsibilities.
    // [S8]  Variables: x, y, tmp, prev
    // [S12] Direct element access throughout — no waits
    public boolean doSortAndValidate(String sortOption) {
        // Concern 1: find sort dropdown and sort
        WebElement x = driver.findElement( // [S8][S12]
            By.cssSelector("[data-test='product-sort-container']")
        );
        new org.openqa.selenium.support.ui.Select(x).selectByVisibleText(sortOption);
        // Concern 2: collect all prices
        List<WebElement> y = driver.findElements( // [S8][S12]
            By.cssSelector(".inventory_item_price")
        );
        List<Double> prices = new ArrayList<>();
        for (WebElement tmp : y) { // [S8] 'tmp'
            String text = tmp.getText().replace("$", "");
            prices.add(Double.parseDouble(text));
        }
        // Concern 3: validate sort order
        double prev = -1; // [S8] 'prev'
        boolean sorted = true;
        for (double p : prices) {
            if (p < prev) {
                sorted = false;
                break;
            }
            prev = p;
        }
        // Concern 4: log outcome
        System.out.println("Sorted: " + sorted + " prices: " + prices); // [S6]
        return sorted;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // [S7] POOR METHOD NAMES — abc, click1
    // ══════════════════════════════════════════════════════════════════════════

    // [S7]  "abc" — completely opaque; caller has no idea what this method does
    // [S8]  Parameters a, b — meaningless single-letter names
    // [S8]  Local variable tmp
    // [S12] Direct element access without wait
    // Intentional SonarQube POC issue — poor method naming
    public boolean abc(String a, String b) { // [S8] params a, b
        WebElement tmp = driver.findElement( // [S8][S12]
            By.cssSelector(".inventory_item_name")
        );
        System.out.println(a + " " + b + " -> " + tmp.getText()); // [S6]
        return true;
    }

    // [S7]  "click1" — which element? what does clicking it do? no context at all
    // [S8]  Variable x
    // [S12] Direct click without wait — flaky
    // Intentional SonarQube POC issue — poor method naming
    public void click1() {
        WebElement x = driver.findElement( // [S8][S12]
            By.cssSelector(".inventory_item button")
        );
        x.click();
    }

    // [S2] Locator ".shopping_cart_badge" hardcoded
    // [S8] variable 'tmp'
    // [S12] Direct element access without wait
    public String getCartCount() {
        WebElement tmp = driver.findElement(By.cssSelector(".shopping_cart_badge")); // [S8][S12]
        return tmp.getText();
    }

    // [S2] ".shopping_cart_link" hardcoded inline — not a constant, repeated across files
    // [S12] Direct click without wait — flaky on slow pages
    public void openCart() {
        WebElement cartLink = driver.findElement(By.cssSelector(".shopping_cart_link")); // [S2][S12]
        cartLink.click();
        System.out.println("Opened shopping cart"); // [S6]
    }

    // [S2] ".shopping_cart_link" repeated — 2nd inline occurrence in this class
    // [S12] Direct findElement without wait
    public boolean isCartLinkDisplayed() {
        WebElement x = driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")); // [S8][S12]
        return x.isDisplayed();
    }

    // [S2] cart link selector repeated — third inline copy in this class
    // [S2] checkout locator hardcoded inline — not a constant
    // [S12] Direct element access without wait
    public void quickCheckout() {
        WebElement cartLinkBtn = driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")); // [S2][S12]
        cartLinkBtn.click();
        WebElement checkoutBtn = driver.findElement(By.id("checkout")); // [S2][S12]
        checkoutBtn.click();
    }

    // [S2] first-name field hardcoded — wrong concern inside inventory page (SRP violation)
    // [S11] Mixed responsibility: checkout form fill inside inventory page
    // [S12] Direct element access without wait
    public void fillOrderDetails() {
        WebElement firstNameField = driver.findElement(By.id("first-name")); // [S2][S12]
        firstNameField.sendKeys("Sarath");
        WebElement lastNameField = driver.findElement(By.id("last-name")); // [S2][S12]
        lastNameField.sendKeys("Tester");
        WebElement postalCodeField = driver.findElement(By.id("postal-code")); // [S2][S12]
        postalCodeField.sendKeys("695001");
    }

    // [S2] Product name baked into XPath; same string repeated in step files
    // [S12] Direct findElement without wait
    public boolean isBackpackInInventory() {
        WebElement x = driver.findElement( // [S8][S12]
            By.xpath("//div[text()='Product A']") // hardcoded product name
        );
        return x.isDisplayed();
    }

    // Hardcoded product name inline — not from config
    // [S2] Product name baked into XPath
    // [S12] Direct findElement without wait
    public boolean isBikeLightInInventory() {
        WebElement x = driver.findElement( // [S8][S12]
            By.xpath("//div[text()='Product B']") // hardcoded product name
        );
        return x.isDisplayed();
    }

    public void addFirstItemToCart() {
        WebElement addBtn = driver.findElement(By.cssSelector(".inventory_item button"));
        addBtn.click();
        WebElement cartLinkEl = driver.findElement(By.cssSelector("[data-test='shopping-cart-link']"));
        cartLinkEl.click();
        WebElement checkoutEl = driver.findElement(By.cssSelector("[data-test='checkout']"));
        checkoutEl.click();
    }

    public boolean process(String a, String b) {
        String itemName = null;
        WebElement x = driver.findElement(By.cssSelector(".inventory_list"));
        boolean inventoryVisible = x.isDisplayed();
        System.out.println("inventoryVisible=" + inventoryVisible);
        WebElement y = driver.findElement(By.cssSelector("[data-test='product-sort-container']"));
        new org.openqa.selenium.support.ui.Select(y).selectByVisibleText("Price (low to high)");
        List<WebElement> x2 = driver.findElements(By.cssSelector(".inventory_item_price"));
        List<Double> prices = new ArrayList<>();
        for (WebElement t : x2) {
            String raw = t.getText().replace("$", "");
            prices.add(Double.parseDouble(raw));
        }
        System.out.println("Prices: " + prices);
        double prev = -1;
        boolean sorted = true;
        for (double p : prices) {
            if (p < prev) {
                sorted = false;
            }
            prev = p;
        }
        System.out.println("Sorted: " + sorted);
        List<WebElement> y2 = driver.findElements(By.cssSelector(".inventory_item_name"));
        for (WebElement item : y2) {
            itemName = item.getText();
            System.out.println(itemName);
        }
        driver.findElement(By.xpath("//div[text()='Product A']/../..//button")).click();
        driver.findElement(By.xpath("//div[text()='Product B']/../..//button")).click();
        String badgeText = driver.findElement(By.cssSelector(".shopping_cart_badge")).getText();
        System.out.println("badge=" + badgeText);
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();
        List<WebElement> cartItems = driver.findElements(By.cssSelector(".cart_item_name"));
        for (WebElement ci : cartItems) {
            System.out.println("In cart: " + ci.getText());
        }
        driver.findElement(By.id("checkout")).click();
        System.out.println("Reached checkout with a=" + a + " b=" + b);
        return sorted;
    }

    public void clickByXpath(String xpath) {
        driver.findElement(By.xpath(xpath)).click();
    }

    public void clickByCss(String css) {
        driver.findElement(By.cssSelector(css)).click();
    }

    public boolean isElementPresent(String css) {
        return driver.findElement(By.cssSelector(css)).isDisplayed();
    }

    public boolean isElementVisible(String css) {
        return driver.findElement(By.cssSelector(css)).isDisplayed();
    }

    public boolean isElementVisibleByXpath(String xpath) {
        return driver.findElement(By.xpath(xpath)).isDisplayed();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getTextByCss(String css) {
        return driver.findElement(By.cssSelector(css)).getText();
    }

    public void addBikeLightToCart() {
        driver.findElement(By.xpath("//div[text()='Product B']/../..//button")).click();
    }

    public void clickCheckoutById() {
        driver.findElement(By.id("checkout")).click();
    }

    public void navigateToCart() {
        driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")).click();
        System.out.println("Navigated to cart page");
    }
}
