package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class InventoryPage extends BasePage {

    private static final By INVENTORY_CONTAINER = By.cssSelector(".inventory_list");
    private static final By SORT_DROPDOWN       = By.cssSelector("[data-test='product-sort-container']");
    private static final By PRODUCT_NAMES       = By.cssSelector(".inventory_item_name");
    private static final By PRODUCT_PRICES      = By.cssSelector(".inventory_item_price");
    private static final By PAGE_TITLE          = By.cssSelector("span.title");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isAt() {
        try {
            waitForVisibility(PAGE_TITLE);
            return "Products".equals(getText(PAGE_TITLE));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isInventoryDisplayed() {
        return isDisplayed(INVENTORY_CONTAINER);
    }

    /**
     * Selects a sort option by its visible label in the dropdown.
     * Valid labels on SauceDemo: "Name (A to Z)", "Name (Z to A)",
     * "Price (low to high)", "Price (high to low)".
     */
    public void sortBy(String visibleText) {
        WebElement dropdown = waitForVisibility(SORT_DROPDOWN);
        new Select(dropdown).selectByVisibleText(visibleText);
    }

    /**
     * Returns each product price as a Double after stripping the leading "$".
     * Waits for at least one price element to be visible before collecting.
     */
    public List<Double> getProductPrices() {
        waitForVisibility(PRODUCT_PRICES);
        return driver.findElements(PRODUCT_PRICES).stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .toList();
    }

    public List<String> getProductNames() {
        waitForVisibility(PRODUCT_NAMES);
        return driver.findElements(PRODUCT_NAMES).stream()
                .map(WebElement::getText)
                .toList();
    }

    public boolean validateSortingLowToHigh() {
        List<Double> prices = getProductPrices();
        for (int i = 0; i < prices.size() - 1; i++) {
            if (prices.get(i) > prices.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    public boolean validateSortingHighToLow() {
        List<Double> prices = getProductPrices();
        for (int i = 0; i < prices.size() - 1; i++) {
            if (prices.get(i) < prices.get(i + 1)) {
                return false;
            }
        }
        return true;
    }
}
