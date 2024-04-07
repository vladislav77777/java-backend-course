package edu.java.configuration;

import edu.java.client.BotClient;
import edu.java.client.ClientLinkUpdateSender;
import edu.java.entity.dto.LinkUpdateRequest;
import edu.java.service.kafka.ScrapperQueueProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class LinkUpdatePublisherConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
    public ScrapperQueueProducer scrapperQueueProducer(
        ApplicationConfig config,
        KafkaTemplate<Long, LinkUpdateRequest> kafkaTemplate
    ) {
        return new ScrapperQueueProducer(config, kafkaTemplate);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
    public ClientLinkUpdateSender clientLinkUpdateSender(BotClient botClient) {
        return new ClientLinkUpdateSender(botClient);
    }
}
