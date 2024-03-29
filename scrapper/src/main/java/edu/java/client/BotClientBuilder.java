package edu.java.client;

import org.springframework.retry.support.RetryTemplate;

public class BotClientBuilder {
    private String baseUrl;
    private RetryTemplate retryTemplate;

    public BotClientBuilder setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public BotClientBuilder setRetryTemplate(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
        return this;
    }

    public BotClient build() {
        return new BotClient(baseUrl, retryTemplate);
    }
}
