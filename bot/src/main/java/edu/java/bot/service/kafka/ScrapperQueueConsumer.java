package edu.java.bot.service.kafka;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.entity.dto.LinkUpdateRequest;
import edu.java.bot.service.LinkUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class ScrapperQueueConsumer {
    private final ApplicationConfig config;
    private final LinkUpdateService linkUpdateService;
    private final KafkaTemplate<Long, LinkUpdateRequest> kafkaTemplate;

    @KafkaListener(groupId = "scrapper.updates.listeners",
                   topics = "${app.kafka-config-info.updates-topic.name}",
                   containerFactory = "linkUpdateRequestConcurrentKafkaListenerContainerFactory")
    public void listen(@Payload LinkUpdateRequest request) {
        try {
            linkUpdateService.sendUpdateNotification(request);
        } catch (Exception e) {
            log.error(e);
            kafkaTemplate.send(config.kafkaConfigInfo().updatesTopic().name() + "_dlq", request.id(), request);
        }
    }
}
