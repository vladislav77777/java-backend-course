package edu.java.configuration;

import edu.java.client.BotClient;
import edu.java.client.BotClientBuilder;
import edu.java.client.GitHubClient;
import edu.java.client.GitHubClientBuilder;
import edu.java.client.StackOverflowClient;
import edu.java.client.StackOverflowClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {
    private final ApplicationConfig config;

    @Bean
    public GitHubClient gitHubClient(RetryTemplate retryTemplate) {
        return new GitHubClientBuilder()
            .setBaseUrl(config.client().gitHub())
            .setRetryTemplate(retryTemplate)
            .build();
    }

    @Bean
    public StackOverflowClient stackOverflowClient(RetryTemplate retryTemplate) {
        return new StackOverflowClientBuilder()
            .setBaseUrl(config.client().stackOverflow())
            .setRetryTemplate(retryTemplate)
            .build();
    }

    @Bean
    public BotClient botClient(RetryTemplate retryTemplate) {
        return new BotClientBuilder()
            .setBaseUrl(config.client().bot())
            .setRetryTemplate(retryTemplate)
            .build();
    }
}

