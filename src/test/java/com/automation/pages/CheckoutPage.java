package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CheckoutPage extends BasePage {

    private static final String FIRST_NAME_ID = "first-name";

    private static final By FIRST_NAME_FIELD  = By.id(FIRST_NAME_ID);
    private static final By LAST_NAME_FIELD   = By.id("last-name");
    private static final By POSTAL_CODE_FIELD = By.id("postal-code");
    private static final By CONTINUE_BUTTON   = By.cssSelector("[data-test='continue']");
    private static final By ERROR_MESSAGE     = By.cssSelector("[data-test='error']");
    private static final By PAGE_TITLE        = By.cssSelector("span.title");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isAt() {
        try {
            waitForVisibility(PAGE_TITLE);
            return getText(PAGE_TITLE).contains("Your Information");
        } catch (TimeoutException e) {
            return false;
        }
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    public CheckoutPage enterFirstName(String firstName) {
        type(FIRST_NAME_FIELD, firstName);
        return this;
    }

    public CheckoutPage enterLastName(String lastName) {
        type(LAST_NAME_FIELD, lastName);
        return this;
    }

    public CheckoutPage enterPostalCode(String postalCode) {
        type(POSTAL_CODE_FIELD, postalCode);
        return this;
    }

    /**
     * Scrolls the Continue button into view and clicks it.
     * The button sits at the bottom of the form and can fall below the headless
     * Chrome viewport (~800×600) after the fields are typed into — causing the same
     * off-screen click failure as the Finish button on the overview page.
     */
    public void clickContinue() {
        WebElement btn = waitForClickability(CONTINUE_BUTTON);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
        js.executeScript("arguments[0].click();", btn);
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(ERROR_MESSAGE);
    }
}
