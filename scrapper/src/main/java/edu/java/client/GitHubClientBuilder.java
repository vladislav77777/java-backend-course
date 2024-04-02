package edu.java.client;

import org.springframework.retry.support.RetryTemplate;

public class GitHubClientBuilder {
    private String baseUrl = "https://api.github.com";
    private RetryTemplate retryTemplate;

    public GitHubClientBuilder setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public GitHubClientBuilder setRetryTemplate(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
        return this;
    }

    public GitHubClient build() {
        return new GitHubClient(baseUrl, retryTemplate);
    }
}
