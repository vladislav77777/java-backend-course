package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.LinkTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListCommand implements Command {

    private final LinkTracker linkTracker;

    @Autowired
    public ListCommand(LinkTracker linkTracker) {
        this.linkTracker = linkTracker;
    }

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

        List<String> trackedLinks = linkTracker.getTrackedLinks(chatId);

        if (trackedLinks.isEmpty()) {
            return new SendMessage(chatId, "The list of tracked links is empty.");
        } else {
            StringBuilder message = new StringBuilder("Tracked Links:\n");
            for (String link : trackedLinks) {
                message.append(link).append("\n");
            }
            return new SendMessage(chatId, message.toString());
        }
    }
}
