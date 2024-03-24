package edu.java.job;

import edu.java.client.BotClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.entity.dto.LinkUpdateRequest;
import edu.java.service.LinkService;
import edu.java.util.client.BaseClientProcessor;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

@Component
@Slf4j
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final List<BaseClientProcessor> clientProcessors;
    private final LinkService linkService;
    private final BotClient botClient;
    private final ApplicationConfig config;

    @Scheduled(fixedDelayString = "PT${app.scheduler.interval}")
    public void update() {
        log.info("Update method was invoked");

        linkService.listAllWithInterval(config.scheduler().linkLastCheckInterval()).forEach(link -> {
            for (BaseClientProcessor clientProcessor : clientProcessors) {
                if (clientProcessor.isCandidate(link.getUrl())) {
                    LinkUpdateRequest s = clientProcessor.getUpdate(link)
                        .filter(Objects::nonNull)
                        .publishOn(Schedulers.boundedElastic())
                        .map(update -> new LinkUpdateRequest(
                            link.getId(),
                            link.getUrl(),
                            update,
                            linkService.getAllChatsForLink(link.getId())
                        )).block();
                    if (s != null) {
                        botClient.sendUpdate(s);
                    }
                    linkService.updateLink(link.setLastUpdatedAt(OffsetDateTime.now()));

                    break;
                }
            }
        });
    }
}
