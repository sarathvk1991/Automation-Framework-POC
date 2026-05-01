package com.automation.pages.badexamples;

// =============================================================================
// INTENTIONALLY NON-COMPLIANT — POC SONARQUBE DEMONSTRATION
//
// SonarQube Issues Demonstrated:
//   [S1]  No base class — WebDriver boilerplate duplicated again
//   [S2]  Locator strings hardcoded inline in every method
//   [S7]  Non-descriptive method names: doCheck(), goCheckout()
//   [S9]  Returning null instead of throwing
//   [S12] Flaky direct element access without explicit wait throughout
//   [S13] Boolean method uses inverted variable name (notFound logic)
// =============================================================================

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import java.util.ArrayList;
import java.util.List;

public class BadCartPage {

    // [S1] Same boilerplate as BadLoginPage and BadInventoryPage
    private WebDriver driver;
    private WebDriverWait wait;

    private static final String CART_LINK_SELECTOR = ".shopping_cart_link";
    private static final By CART_LINK = By.cssSelector(CART_LINK_SELECTOR);
    private static final String CHECKOUT_BUTTON_ID = "checkout";
    private static final By CHECKOUT_BTN = By.id(CHECKOUT_BUTTON_ID);

    public BadCartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── [S7] Opaque method names ───────────────────────────────────────────────

    // [S7] "getStuff" — stuff? what stuff?
    // [S2] ".cart_item_name" hardcoded inline
    // [S12] Direct findElements with no explicit wait — unstable in slow environments
    public List<String> getStuff() {
        try {
            List<String> itemNames = new ArrayList<>();
            List<WebElement> cartItems = driver.findElements(
                By.cssSelector(".cart_item_name")
            );
            for (WebElement item : cartItems) {
                itemNames.add(item.getText());
            }
            return itemNames;
        } catch (WebDriverException e) {
            return new ArrayList<>();
        }
    }

    // ── [S13] Name vs behaviour mismatch ─────────────────────────────────────

    // [S13] Method returns true when item IS found but variable is named "notFound"
    // [S12] Direct findElements without wait
    public boolean doCheck(String itemName) {
        List<WebElement> cartItems = driver.findElements( // [S12]
            By.cssSelector(".cart_item_name")
        );
        boolean notFound = true; // [S13] misleading variable — actually means "searching"
        for (WebElement element : cartItems) {
            if (element.getText().equals(itemName)) {
                notFound = false; // [S13] set to false when found — confusing inversion
            }
        }
        return !notFound; // [S13] double negation hides intent
    }

    // Was duplicate of doCheck() — now delegates to helper to eliminate duplication
    public boolean verify(String itemName) {
        return containsItemInCart(itemName);
    }

    // Private helper extracted from doCheck/verify duplicate to resolve S4144
    private boolean containsItemInCart(String itemName) {
        List<WebElement> cartItems = driver.findElements( // [S12]
            By.cssSelector(".cart_item_name")
        );
        for (WebElement element : cartItems) {
            if (element.getText().equals(itemName)) {
                return true;
            }
        }
        return false;
    }

    // ── [S11] Long method ── [S2] Repeated locators ─────────────────────────

    // [S11] Reads cart title, counts items, finds specific item, clicks checkout — mixed responsibility
    // [S2] ".cart_item" and ".cart_item_name" repeated (same as getStuff above)
    // [S12] All interactions direct — no wait
    public String doEverything(String expected) {
        try {
            String foundItem = null; // [S9]
            // Concern 1: check cart title is visible
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.title")));
            System.out.println("Title: " + title.getText()); // [S6]
            // Concern 2: count items
            List<WebElement> items = driver.findElements(By.cssSelector(".cart_item"));
            System.out.println("Cart has " + items.size() + " items"); // [S6]
            // Concern 3: find specific item name
            List<WebElement> names = driver.findElements(By.cssSelector(".cart_item_name"));
            for (WebElement item : names) {
                if (item.getText().contains(expected)) {
                    foundItem = item.getText();
                }
            }
            // Concern 4: click checkout
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='checkout']" ))).click();
            return foundItem; // [S9] may return null
        } catch (WebDriverException e) {
            return null;
        }
    }

    // [S2] "[data-test='checkout']" hardcoded — already used in doEverything
    // [S12] Direct click without wait
    public void goCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='checkout']" ))).click();
    }

    // [S2] ".cart_item" repeated a third time in this class
    // [S12] Direct findElements without wait
    public boolean isEmpty() {
        List<WebElement> cartItems = driver.findElements(By.cssSelector(".cart_item")); // [S12]
        return cartItems.size() == 0;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Previously poorly-named methods — renamed for clarity
    // ══════════════════════════════════════════════════════════════════════════

    // Previously "doIt" — renamed to getCartListDetails
    // [S12] Direct element access without wait — flaky on slow pages
    public String getCartListDetails() {
        WebElement cartListEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart_list")));
        String itemText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart_item_name"))).getText();
        System.out.println(cartListEl.isDisplayed() + " item: " + itemText); // [S6]
        return itemText;
    }

    // Previously "process" — renamed to getAllCartItemNames
    // [S12] Direct findElements without wait
    public List<String> getAllCartItemNames() {
        List<String> itemNames = new ArrayList<>();
        List<WebElement> cartItems = driver.findElements(By.cssSelector(".cart_item_name")); // [S12]
        for (WebElement element : cartItems) {
            itemNames.add(element.getText());
        }
        return itemNames;
    }

    // Intentional SonarQube POC issue — direct element click with no wait (flaky)
    public void removeFirstItem() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".cart_item .btn_secondary"))).click();
    }

    // Intentional SonarQube POC issue — direct findElement click without any wait (flaky)
    public boolean continueShopping() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='continue-shopping']" ))).click();
        return true;
    }

    // [S2] checkout locator hardcoded inline — not a constant
    // [S12] Direct element click without wait
    public void proceedToCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(CHECKOUT_BTN)).click();
    }

    // [S2] cart link selector hardcoded inline — not a constant
    // [S2] checkout locator repeated — second inline copy in this class
    // [S12] Direct element access without wait
    public boolean isCartAccessible() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='shopping-cart-link']" ))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='checkout']" ))).click();
        return true;
    }

    // [S12] Direct findElement without wait — flaky
    public boolean verifyExpectedItems() {
        WebElement backpack = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[text()='Sauce Labs Backpack']")
        ));
        WebElement bikeLight = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[text()='Sauce Labs Bike Light']")
        ));
        System.out.println("Backpack: " + backpack.isDisplayed());                // [S6]
        System.out.println("Bike Light: " + bikeLight.isDisplayed());             // [S6]
        return backpack.isDisplayed() && bikeLight.isDisplayed();
    }

    public String getFirstItemPrice() {
        WebElement priceEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inventory_item_price")));
        return priceEl.getText();
    }

    public int getCartItemCount() {
        return driver.findElements(By.cssSelector(".cart_item")).size();
    }

    public void clickCartLink() {
        wait.until(ExpectedConditions.elementToBeClickable(CART_LINK)).click();
    }
}
