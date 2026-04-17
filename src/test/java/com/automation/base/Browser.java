package com.automation.base;

/**
 * Supported browsers.
 * Add new values here when support for additional browsers is needed.
 */
public enum Browser {

    CHROME, FIREFOX, EDGE;

    /**
     * Resolves a browser name string (case-insensitive) to its enum constant.
     * Centralises validation so callers never work with raw strings.
     */
    public static Browser from(String name) {
        return switch (name.trim().toLowerCase()) {
            case "chrome"  -> CHROME;
            case "firefox" -> FIREFOX;
            case "edge"    -> EDGE;
            default        -> throw new IllegalArgumentException(
                    "Unsupported browser: '" + name + "'. Valid options: chrome, firefox, edge.");
        };
    }
}
