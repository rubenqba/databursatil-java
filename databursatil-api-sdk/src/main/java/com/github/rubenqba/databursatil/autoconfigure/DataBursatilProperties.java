package com.github.rubenqba.databursatil.autoconfigure;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "databursatil")
public class DataBursatilProperties {

    /**
     * Enable or disable the StockMarketService auto-configuration.
     */
    private boolean enabled = true;

    /**
     * Base URL for the API.
     */
    private String baseUrl = "https://api.databursatil.com/v1";

    /**
     * API token (required).
     */
    @NotBlank
    private String apiToken;

    // getters and setters
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }
}