package com.automation.utils;

/**
 * Centralised test data constants for bad-example POC classes.
 * Values are loaded from config.properties so no literal test data
 * appears in source code (fixes customqa:hardcoded-test-data).
 */
public class TestData {

    public static final String USERNAME           = ConfigReader.get("test.username");
    public static final String PASSWORD           = ConfigReader.get("test.password");
    public static final String PRODUCT_BACKPACK   = ConfigReader.get("test.product.backpack");
    public static final String PRODUCT_BIKE_LIGHT = ConfigReader.get("test.product.bike_light");
    public static final String FIRST_NAME         = ConfigReader.get("test.first_name");
    public static final String LAST_NAME          = ConfigReader.get("test.last_name");
    public static final String ZIP_CODE           = ConfigReader.get("test.zip_code");

    private TestData() {}
}
