package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.entity.dto.LinkResponse;
import edu.java.bot.exception.ApiErrorResponseException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {

    private final ScrapperClient client;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "Show the list of tracked links";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        return new SendMessage(chatId, getResponseMessage(chatId))
            .disableWebPagePreview(true);
    }

    private String getResponseMessage(Long chatId) {
        return client.getAllLinksForChat(chatId)
            .map(response -> {
                if (HttpStatus.OK.equals(response.getStatusCode())
                    && response.getBody() != null && response.getBody().links() != null) {
                    if (response.getBody().links().isEmpty()) {
                        return "The list of tracked links is empty.";
                    }

                    return buildListOfLinks(response.getBody().links());
                }

                return "Something went wrong :(";
            })
            .onErrorResume(
                ApiErrorResponseException.class,
                error -> Mono.just(error.getApiErrorResponse().description())
            )
            .block();
    }

    @NotNull private static String buildListOfLinks(List<LinkResponse> links) {
        List<String> urls = links.stream().map(linkResponse -> linkResponse.url().toString()).toList();

        StringBuilder message = new StringBuilder("Tracked Links:\n");
        for (int i = 0; i < urls.size(); i++) {
            message.append(i + 1).append(": ").append(urls.get(i)).append("\n");
        }
        return message.toString();
    }
}
