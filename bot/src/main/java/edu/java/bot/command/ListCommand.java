package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.UserChat;
import edu.java.bot.repository.LinkTracker;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {

    private final LinkTracker repository;

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
        UserChat userChat = repository.findById(chatId);
        List<String> links = userChat.getTrackingLinks();

        if (links.isEmpty()) {
            return new SendMessage(chatId, "The list of tracked links is empty.");
        }

        return new SendMessage(chatId, buildListOfLinks(links));

    }

    @NotNull private static String buildListOfLinks(List<String> links) {
        StringBuilder message = new StringBuilder("Tracked Links:\n");
        for (String link : links) {
            message.append(link).append("\n");
        }
        return message.toString();
    }
}
