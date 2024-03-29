package edu.java.client;

import org.springframework.retry.support.RetryTemplate;

public class StackOverflowClientBuilder {
    private String baseUrl = "https://api.stackexchange.com/2.3";
    private RetryTemplate retryTemplate;

    public StackOverflowClientBuilder setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public StackOverflowClientBuilder setRetryTemplate(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
        return this;
    }

    public StackOverflowClient build() {
        return new StackOverflowClient(baseUrl, retryTemplate);
    }
}
