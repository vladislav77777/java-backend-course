package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.entity.dto.AddLinkRequest;
import edu.java.bot.exception.ApiErrorResponseException;
import edu.java.bot.util.LinkUtil;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TrackCommand implements Command {
    private final ScrapperClient client;

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Start tracking a link \uD83D\uDD0D";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();

        String messageText = update.message().text();
        String[] tokens = messageText.split("\\s+");

        if (tokens.length < 2) {
            return new SendMessage(update.message().chat().id(), "Please provide a link to track.");
        }
        URI link = LinkUtil.parse(tokens[1]);
        if (link == null) {
            return new SendMessage(chatId, "Incorrect link");
        }

        return new SendMessage(chatId, getResponseMessage(chatId, link))
            .disableWebPagePreview(true);
    }

    private String getResponseMessage(Long chatId, URI link) {
        return client.addLink(chatId, new AddLinkRequest(link))
            .map(response -> {
                if (HttpStatus.OK.equals(response.getStatusCode())
                    && response.getBody() != null) {
                    return "☑\uFE0F Tracking started for the link: %s"
                        .formatted(response.getBody().url());
                }

                return "Something went wrong  \uD83D\uDE1E";
            })
            .onErrorResume(
                ApiErrorResponseException.class,
                error -> Mono.just(error.getApiErrorResponse().description())
            )
            .block();
    }
}
