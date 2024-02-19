package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.LinkTracker;

public class UntrackCommand implements Command {

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
        String messageText = update.message().text();
        String[] tokens = messageText.split("\\s+");

        if (tokens.length < 2) {
            return new SendMessage(update.message().chat().id(), "Please provide a link to untrack.");
        }

        String link = tokens[1];
        LinkTracker.untrackLink(update.message().chat().id(), link);

        return new SendMessage(update.message().chat().id(), "Tracking stopped for the link: " + link);
    }
}
