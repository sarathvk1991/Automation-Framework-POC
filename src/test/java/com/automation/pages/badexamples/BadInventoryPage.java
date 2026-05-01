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
        try {
            WebElement inventoryListEl = driver.findElement(By.cssSelector(".inventory_list")); // [S12]
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
        WebElement sortDropdown = driver.findElement( // [S12]
            By.cssSelector("[data-test='product-sort-container']")
        );
        new org.openqa.selenium.support.ui.Select(sortDropdown).selectByVisibleText(sortOptionText);
    }

    // ── [S7] Opaque method name ─────────────────────────────────────────────

    // [S7] "doSort" — sort by what? ascending or descending?
    // [S2] Locator "[data-test='product-sort-container']" repeated (same as selectSortOption)
    // [S12] Direct element access without wait
    public void doSort(String sortText) {
        WebElement sortDropdown = driver.findElement( // [S12]
            By.cssSelector("[data-test='product-sort-container']")
        );
        new org.openqa.selenium.support.ui.Select(sortDropdown).selectByVisibleText(sortText);
    }

    // ── [S10] Duplicate method bodies — refactored ────────────────────────────

    // Private helper extracted from getNames/fetchNames duplicate to resolve S4144
    private List<String> collectItemNames() {
        List<String> names = new ArrayList<>();
        List<WebElement> itemElements = driver.findElements( // [S12]
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
        WebElement sortDropdown = driver.findElement( // [S12]
            By.cssSelector("[data-test='product-sort-container']")
        );
        new org.openqa.selenium.support.ui.Select(sortDropdown).selectByVisibleText(sortOption);
        // Concern 2: collect all prices
        List<WebElement> priceElements = driver.findElements( // [S12]
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
        WebElement inventoryItemEl = driver.findElement( // [S12]
            By.cssSelector(".inventory_item_name")
        );
        System.out.println(itemName + " " + category + " -> " + inventoryItemEl.getText()); // [S6]
        return true;
    }

    // Previously "click1" — renamed to clickFirstAddToCartButton for clarity
    // [S12] Direct click without wait — flaky
    public void clickFirstAddToCartButton() {
        WebElement addToCartBtn = driver.findElement( // [S12]
            By.cssSelector(".inventory_item button")
        );
        addToCartBtn.click();
    }

    // [S2] Locator ".shopping_cart_badge" hardcoded
    // [S12] Direct element access without wait
    public String getCartCount() {
        WebElement badgeElement = driver.findElement(By.cssSelector(".shopping_cart_badge")); // [S12]
        return badgeElement.getText();
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
        WebElement cartLinkEl = driver.findElement(By.cssSelector("[data-test='shopping-cart-link']")); // [S12]
        return cartLinkEl.isDisplayed();
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
        WebElement backpackEl = driver.findElement( // [S12]
            By.xpath("//div[text()='Product A']") // hardcoded product name
        );
        return backpackEl.isDisplayed();
    }

    // Hardcoded product name inline — not from config
    // [S2] Product name baked into XPath
    // [S12] Direct findElement without wait
    public boolean isBikeLightInInventory() {
        WebElement bikeLightEl = driver.findElement( // [S12]
            By.xpath("//div[text()='Product B']") // hardcoded product name
        );
        return bikeLightEl.isDisplayed();
    }

    public void addFirstItemToCart() {
        WebElement addBtn = driver.findElement(By.cssSelector(".inventory_item button"));
        addBtn.click();
        WebElement cartLinkEl = driver.findElement(By.cssSelector("[data-test='shopping-cart-link']"));
        cartLinkEl.click();
        WebElement checkoutEl = driver.findElement(By.cssSelector("[data-test='checkout']"));
        checkoutEl.click();
    }

    // Previously "process" — renamed to sortInventoryAndProceedToCheckout for clarity
    public boolean sortInventoryAndProceedToCheckout(String sortOption, String expectedItem) {
        String itemName = null;
        WebElement inventoryListEl = driver.findElement(By.cssSelector(".inventory_list"));
        boolean inventoryVisible = inventoryListEl.isDisplayed();
        System.out.println("inventoryVisible=" + inventoryVisible);
        WebElement sortDropdown = driver.findElement(By.cssSelector("[data-test='product-sort-container']"));
        new org.openqa.selenium.support.ui.Select(sortDropdown).selectByVisibleText("Price (low to high)");
        List<WebElement> priceElements = driver.findElements(By.cssSelector(".inventory_item_price"));
        List<Double> prices = new ArrayList<>();
        for (WebElement priceEl : priceElements) {
            String raw = priceEl.getText().replace("$", "");
            prices.add(Double.parseDouble(raw));
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
        List<WebElement> itemElements = driver.findElements(By.cssSelector(".inventory_item_name"));
        for (WebElement item : itemElements) {
            itemName = item.getText();
            System.out.println(itemName);
        }
        driver.findElement(By.xpath("//div[text()='Product A']/../..//button")).click();
        driver.findElement(By.xpath("//div[text()='Product B']/../..//button")).click();
        String badgeText = driver.findElement(By.cssSelector(".shopping_cart_badge")).getText();
        System.out.println("badge=" + badgeText);
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();
        List<WebElement> cartItems = driver.findElements(By.cssSelector(".cart_item_name"));
        for (WebElement cartItem : cartItems) {
            System.out.println("In cart: " + cartItem.getText());
        }
        driver.findElement(By.id("checkout")).click();
        System.out.println("Reached checkout with sortOption=" + sortOption + " expectedItem=" + expectedItem);
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
