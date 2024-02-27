package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.UserChat;
import edu.java.bot.repository.LinkTracker;
import edu.java.bot.util.LinkUtil;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UntrackCommand implements Command {
    private final LinkTracker repository;

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Stop tracking a link";
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
        UserChat userChat = repository.findById(chatId);
        List<String> links = userChat.getTrackingLinks();

        if (!links.contains(link.toString())) {
            return new SendMessage(chatId, "Link is not tracked");

        }

        links.remove(link.toString());
        repository.add(new UserChat(userChat.getChatId(), links));

        return new SendMessage(update.message().chat().id(), "Tracking stopped for the link: " + link);
    }
}
