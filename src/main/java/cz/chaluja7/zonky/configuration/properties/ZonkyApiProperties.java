package cz.chaluja7.zonky.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "zonky.api")
public class ZonkyApiProperties {

    private String baseUrl;

    private String userAgent;

    private Integer batchPageSize;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Integer getBatchPageSize() {
        return batchPageSize;
    }

    public void setBatchPageSize(Integer batchPageSize) {
        this.batchPageSize = batchPageSize;
    }

}



