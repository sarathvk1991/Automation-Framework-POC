package com.automation.pages.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// SonarQube Issues Demonstrated:
//   [S1]  No base class — boilerplate WebDriver code duplicated from BadLoginPage
//   [S2]  Locator strings repeated inline in every method (no constants)
//   [S6]  System.out.println instead of logger
//   [S7]  Non-descriptive method names: getData(), doSort(), doSortAndValidate()
//   [S10] Duplicate method bodies (getNames and fetchNames — now refactored)
//   [S11] Long method mixing sort + collect + assert in one call
//   [S12] Flaky direct element access without explicit wait throughout
// =============================================================================

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import com.automation.utils.TestData;

import java.util.ArrayList;
import java.util.List;

public class BadInventoryPage {

    // [S1] Copy-pasted from BadLoginPage — no shared base class
    private WebDriver driver;
    private WebDriverWait wait;

    private static final String CART_LINK_SELECTOR = ".shopping_cart_link";
    private static final By CART_LINK = By.cssSelector(CART_LINK_SELECTOR);
    private static final String CHECKOUT_BUTTON_ID = "checkout";
    private static final By CHECKOUT_BTN = By.id(CHECKOUT_BUTTON_ID);
    private static final String FIRST_NAME_LOCATOR_ID = "first-name";
    private static final By FIRST_NAME_FIELD = By.id(FIRST_NAME_LOCATOR_ID);

    public BadInventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── [S7][S2] Non-descriptive method, repeated locator ─────────────────────

    // [S7] "getData" — data of what? returns what type? completely opaque
    // [S2] ".inventory_list" hardcoded — not a constant
    // [S12] Direct element access without wait
    public boolean getData() {
        try {
            WebElement inventoryListEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inventory_list")));
            return inventoryListEl.isDisplayed();
        } catch (WebDriverException e) {
            return false;
        }
    }

    // ── [S7] Non-descriptive ── [S2] Repeated locator ─────────────────────────

    // Previously "click2" — renamed to selectSortOption for clarity
    // [S2] "[data-test='product-sort-container']" hardcoded, repeated below
    // [S12] Direct element interaction without wait
    public void selectSortOption(String sortOptionText) {
        WebElement sortDropdown = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("[data-test='product-sort-container']")
        ));
        new org.openqa.selenium.support.ui.Select(sortDropdown).selectByVisibleText(sortOptionText);
    }

    // ── [S7] Opaque method name ─────────────────────────────────────────────

    // [S7] "doSort" — sort by what? ascending or descending?
    // [S2] Locator "[data-test='product-sort-container']" repeated (same as selectSortOption)
    // [S12] Direct element access without wait
    public void doSort(String sortText) {
        WebElement sortDropdown = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("[data-test='product-sort-container']")
        ));
        new org.openqa.selenium.support.ui.Select(sortDropdown).selectByVisibleText(sortText);
    }

    // ── [S10] Duplicate method bodies — refactored ────────────────────────────

    // Private helper extracted from getNames/fetchNames duplicate to resolve S4144
    private List<String> collectItemNames() {
        List<String> names = new ArrayList<>();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inventory_item_name")));
        List<WebElement> itemElements = driver.findElements(
            By.cssSelector(".inventory_item_name")
        );
        for (WebElement element : itemElements) {
            names.add(element.getText());
        }
        return names;
    }

    // [S2] ".inventory_item_name" hardcoded
    // [S12] Direct findElements without wait
    public List<String> getNames() {
        try {
            return collectItemNames();
        } catch (WebDriverException e) {
            return new ArrayList<>();
        }
    }

    // Was duplicate of getNames() — now delegates to helper to eliminate duplication
    public List<String> fetchNames() {
        return collectItemNames();
    }

    // ── [S11] Long method mixing multiple responsibilities ─────────────────────

    // [S11] This single method: sorts, collects prices, validates order, logs result.
    //       Four distinct concerns in one method — far too many responsibilities.
    // [S12] Direct element access throughout — no waits
    public boolean doSortAndValidate(String sortOption) {
        // Concern 1: find sort dropdown and sort
        WebElement sortDropdown = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("[data-test='product-sort-container']")
        ));
        new org.openqa.selenium.support.ui.Select(sortDropdown).selectByVisibleText(sortOption);
        // Concern 2: collect all prices
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inventory_item_price")));
        List<WebElement> priceElements = driver.findElements(
            By.cssSelector(".inventory_item_price")
        );
        List<Double> prices = new ArrayList<>();
        for (WebElement priceEl : priceElements) {
            String text = priceEl.getText().replace("$", "");
            prices.add(Double.parseDouble(text));
        }
        // Concern 3: validate sort order
        double previousPrice = -1;
        boolean sorted = true;
        for (double price : prices) {
            if (price < previousPrice) {
                sorted = false;
                break;
            }
            previousPrice = price;
        }
        // Concern 4: log outcome
        System.out.println("Sorted: " + sorted + " prices: " + prices); // [S6]
        return sorted;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Previously poorly-named methods — renamed for clarity
    // ══════════════════════════════════════════════════════════════════════════

    // Previously "abc" — renamed to logInventoryItemDetails for clarity
    // [S12] Direct element access without wait
    public boolean logInventoryItemDetails(String itemName, String category) {
        WebElement inventoryItemEl = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector(".inventory_item_name")
        ));
        System.out.println(itemName + " " + category + " -> " + inventoryItemEl.getText()); // [S6]
        return true;
    }

    // Previously "click1" — renamed to clickFirstAddToCartButton for clarity
    // [S12] Direct click without wait — flaky
    public void clickFirstAddToCartButton() {
        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector(".inventory_item button")
        ));
        addToCartBtn.click();
    }

    // [S2] Locator ".shopping_cart_badge" hardcoded
    // [S12] Direct element access without wait
    public String getCartCount() {
        WebElement badgeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".shopping_cart_badge")));
        return badgeElement.getText();
    }

    // [S2] ".shopping_cart_link" hardcoded inline — not a constant, repeated across files
    // [S12] Direct click without wait — flaky on slow pages
    public void openCart() {
        wait.until(ExpectedConditions.elementToBeClickable(CART_LINK)).click();
        System.out.println("Opened shopping cart"); // [S6]
    }

    // [S2] ".shopping_cart_link" repeated — 2nd inline occurrence in this class
    // [S12] Direct findElement without wait
    public boolean isCartLinkDisplayed() {
        WebElement cartLinkEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='shopping-cart-link']")));
        return cartLinkEl.isDisplayed();
    }

    // [S2] cart link selector repeated — third inline copy in this class
    // [S2] checkout locator hardcoded inline — not a constant
    // [S12] Direct element access without wait
    public void quickCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='shopping-cart-link']" ))).click();
        wait.until(ExpectedConditions.elementToBeClickable(CHECKOUT_BTN)).click();
    }

    // [S2] first-name field hardcoded — wrong concern inside inventory page (SRP violation)
    // [S11] Mixed responsibility: checkout form fill inside inventory page
    // [S12] Direct element access without wait
    public void fillOrderDetails() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_NAME_FIELD)).sendKeys(TestData.FIRST_NAME);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("last-name"))).sendKeys(TestData.LAST_NAME);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("postal-code"))).sendKeys(TestData.ZIP_CODE);
    }

    // [S2] Product name baked into XPath; same string repeated in step files
    // [S12] Direct findElement without wait
    public boolean isBackpackInInventory() {
        WebElement backpackEl = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[text()='Product A']")
        ));
        return backpackEl.isDisplayed();
    }

    // Hardcoded product name inline — not from config
    // [S2] Product name baked into XPath
    // [S12] Direct findElement without wait
    public boolean isBikeLightInInventory() {
        WebElement bikeLightEl = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[text()='Product B']")
        ));
        return bikeLightEl.isDisplayed();
    }

    public void addFirstItemToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".inventory_item button"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='shopping-cart-link']" ))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='checkout']" ))).click();
    }

    // Helper: sort by price and validate ascending order
    private boolean sortAndValidatePrices() {
        WebElement sortDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='product-sort-container']")));
        new org.openqa.selenium.support.ui.Select(sortDropdown).selectByVisibleText("Price (low to high)");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inventory_item_price")));
        List<WebElement> priceElements = driver.findElements(By.cssSelector(".inventory_item_price"));
        List<Double> prices = new ArrayList<>();
        for (WebElement priceEl : priceElements) {
            prices.add(Double.parseDouble(priceEl.getText().replace("$", "")));
        }
        System.out.println("Prices: " + prices);
        double previousPrice = -1;
        boolean sorted = true;
        for (double price : prices) {
            if (price < previousPrice) {
                sorted = false;
            }
            previousPrice = price;
        }
        System.out.println("Sorted: " + sorted);
        return sorted;
    }

    // Helper: print all inventory item names to stdout
    private void printInventoryItemNames() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inventory_item_name")));
        List<WebElement> itemElements = driver.findElements(By.cssSelector(".inventory_item_name"));
        for (WebElement item : itemElements) {
            System.out.println(item.getText());
        }
    }

    // Helper: add two products, navigate to cart, proceed to checkout
    private void addItemsAndProceedToCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Product A']/../..//button"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Product B']/../..//button"))).click();
        String badgeText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".shopping_cart_badge"))).getText();
        System.out.println("badge=" + badgeText);
        wait.until(ExpectedConditions.elementToBeClickable(CART_LINK)).click();
        List<WebElement> cartItems = driver.findElements(By.cssSelector(".cart_item_name"));
        for (WebElement cartItem : cartItems) {
            System.out.println("In cart: " + cartItem.getText());
        }
        wait.until(ExpectedConditions.elementToBeClickable(CHECKOUT_BTN)).click();
    }

    // Previously "process" — renamed to sortInventoryAndProceedToCheckout for clarity
    // [S11] Original long method — now delegates to private helpers to reduce method length
    public boolean sortInventoryAndProceedToCheckout(String sortOption, String expectedItem) {
        WebElement inventoryListEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inventory_list")));
        boolean inventoryVisible = inventoryListEl.isDisplayed();
        System.out.println("inventoryVisible=" + inventoryVisible);
        boolean sorted = sortAndValidatePrices();
        printInventoryItemNames();
        addItemsAndProceedToCheckout();
        System.out.println("Reached checkout with sortOption=" + sortOption + " expectedItem=" + expectedItem);
        return sorted;
    }

    public void clickByXpath(String xpath) {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
    }

    public void clickByCss(String css) {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(css))).click();
    }

    public boolean isElementPresent(String css) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(css))).isDisplayed();
    }

    public boolean isElementVisible(String css) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(css))).isDisplayed();
    }

    public boolean isElementVisibleByXpath(String xpath) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))).isDisplayed();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getTextByCss(String css) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(css))).getText();
    }

    public void addBikeLightToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Product B']/../..//button"))).click();
    }

    public void clickCheckoutById() {
        wait.until(ExpectedConditions.elementToBeClickable(CHECKOUT_BTN)).click();
    }

    public void navigateToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='shopping-cart-link']"))).click();
        System.out.println("Navigated to cart page");
    }
}
