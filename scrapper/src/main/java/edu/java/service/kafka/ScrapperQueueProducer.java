package edu.java.service.kafka;

import edu.java.configuration.ApplicationConfig;
import edu.java.entity.dto.LinkUpdateRequest;
import edu.java.service.LinkUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class ScrapperQueueProducer implements LinkUpdateService {
    private final ApplicationConfig config;
    private final KafkaTemplate<Long, LinkUpdateRequest> kafkaTemplate;

    @Override
    public void sendUpdate(LinkUpdateRequest request) {
        kafkaTemplate.send(config.kafkaConfigInfo().updatesTopic().name(), request.id(), request);
    }
}
