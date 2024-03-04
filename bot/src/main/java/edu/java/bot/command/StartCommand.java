package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.exception.ApiErrorResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class StartCommand implements Command {
    private final ScrapperClient client;

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "Register the user";
    }

    @Override
    public SendMessage handle(Update update) {
        // Implementation for handling the /start command
        Chat chat = update.message().chat();
        return new SendMessage(update.message().chat().id(), getResponseMessage(chat));
    }

    private String getResponseMessage(Chat chat) {

        return "Welcome, %s!\n".formatted(chat.username())
            + client.registerChat(chat.id())
            .map(response -> {
                if (response.getStatusCode().equals(HttpStatus.OK)) {
                    return "You have been successfully registered!\n";
                }

                return "Something went wrong :(\n";
            })
            .onErrorResume(
                ApiErrorResponseException.class,
                error -> Mono.just(error.getApiErrorResponse().description() + "\n")
            )
            .block()
            + "You can view the available commands using the /help command";
    }
}
