package edu.java.bot.configuration;

import edu.java.bot.configuration.retry.HttpStatusCodeRetryPolicy;
import edu.java.bot.configuration.retry.LinearBackOffPolicy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "app", name = "retry.type", havingValue = "constant")
    public RetryTemplate constantRetryTemplate(ApplicationConfig config) {
        RetryTemplate template = new RetryTemplate();
        HttpStatusCodeRetryPolicy retryPolicy = new HttpStatusCodeRetryPolicy(
            config.retry().maxAttempts(),
            config.retry().retryStatusCodes()
        );
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();

        backOffPolicy.setBackOffPeriod(config.retry().delayConfig().constant().backOffPeriodMillis());

        template.setRetryPolicy(retryPolicy);
        template.setBackOffPolicy(backOffPolicy);

        return template;
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "retry.type", havingValue = "linear")
    public RetryTemplate linearRetryTemplate(ApplicationConfig config) {
        RetryTemplate template = new RetryTemplate();

        HttpStatusCodeRetryPolicy retryPolicy = new HttpStatusCodeRetryPolicy(
            config.retry().maxAttempts(),
            config.retry().retryStatusCodes()
        );
        LinearBackOffPolicy backOffPolicy = new LinearBackOffPolicy();
        backOffPolicy.setInitialInterval(config.retry().delayConfig().linear().initialIntervalMillis());
        backOffPolicy.setMaxInterval(config.retry().delayConfig().linear().maxIntervalMillis());

        template.setRetryPolicy(retryPolicy);
        template.setBackOffPolicy(backOffPolicy);

        return template;
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "retry.type", havingValue = "exponential")
    public RetryTemplate exponentialRetryTemplate(ApplicationConfig config) {
        RetryTemplate template = new RetryTemplate();
        HttpStatusCodeRetryPolicy retryPolicy = new HttpStatusCodeRetryPolicy(
            config.retry().maxAttempts(),
            config.retry().retryStatusCodes()
        );
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();

        backOffPolicy.setInitialInterval(config.retry().delayConfig().exponential().initialIntervalMillis());
        backOffPolicy.setMultiplier(config.retry().delayConfig().exponential().multiplier());
        backOffPolicy.setMaxInterval(config.retry().delayConfig().exponential().maxIntervalMillis());

        template.setRetryPolicy(retryPolicy);
        template.setBackOffPolicy(backOffPolicy);

        return template;
    }
}
