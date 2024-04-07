package edu.java.bot.service;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.bot.TelegramBOT;
import edu.java.bot.entity.dto.LinkUpdateRequest;
import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LinkUpdateService {
    private final TelegramBOT bot;
    private final Counter processedMessagesCounter;

    public void sendUpdateNotification(LinkUpdateRequest request) {
        request.tgChatIds()
            .forEach(tgChatId -> {
                log.info("Send an update to chat with id %d".formatted(tgChatId));

                bot.execute(new SendMessage(
                    tgChatId,
                    "The [link] (%s) was updated:\n%s".formatted(
                        request.url().toString(),
                        request.description()
                    )
                ));
            });
        processedMessagesCounter.increment();
    }
}
