package edu.java.bot.configuration;

import edu.java.bot.client.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {
    private final ApplicationConfig config;

    @Bean
    public ScrapperClient scrapperClient(RetryTemplate retryTemplate) {
        return new ScrapperClient(config.client().scrapper(), retryTemplate);
    }

}
