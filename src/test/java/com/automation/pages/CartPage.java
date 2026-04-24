package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends BasePage {

    private static final By PAGE_TITLE      = By.cssSelector("span.title");
    private static final By CART_ITEM       = By.cssSelector(".cart_item");
    private static final By CART_ITEM_NAME  = By.cssSelector(".cart_item .inventory_item_name");
    private static final By CHECKOUT_BUTTON = By.cssSelector("[data-test='checkout']");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isAt() {
        try {
            waitForVisibility(PAGE_TITLE);
            return "Your Cart".equals(getText(PAGE_TITLE));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Returns the visible name text of every item currently in the cart.
     * Returns an empty list when the cart is empty — never throws.
     */
    public List<String> getCartItems() {
        return driver.findElements(CART_ITEM_NAME).stream()
                .map(WebElement::getText)
                .toList();
    }

    public boolean isProductPresent(String productName) {
        return getCartItems().contains(productName);
    }

    public boolean isCartEmpty() {
        return driver.findElements(CART_ITEM).isEmpty();
    }

    /**
     * Clicks the Checkout button and returns the resulting CheckoutPage.
     */
    public CheckoutPage clickCheckout() {
        click(CHECKOUT_BUTTON);
        return new CheckoutPage(driver);
    }
}
