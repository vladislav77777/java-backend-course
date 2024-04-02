package edu.java.bot.client;

import org.springframework.retry.support.RetryTemplate;

public class ScrapperClientBuilder {
    private String baseUrl = "http://localhost:8080";
    private RetryTemplate retryTemplate;

    public ScrapperClientBuilder setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public ScrapperClientBuilder setRetryTemplate(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
        return this;
    }

    public ScrapperClient build() {
        return new ScrapperClient(baseUrl, retryTemplate);
    }
}
