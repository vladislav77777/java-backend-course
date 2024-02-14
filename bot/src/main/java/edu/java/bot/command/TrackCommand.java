package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.LinkTracker;

public class TrackCommand implements Command {

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Start tracking a link";
    }

    @Override
    public SendMessage handle(Update update) {
        String messageText = update.message().text();
        String[] tokens = messageText.split("\\s+");

        if (tokens.length < 2) {
            return new SendMessage(update.message().chat().id(), "Please provide a link to track.");
        }

        String link = tokens[1];
        LinkTracker.trackLink(update.message().chat().id(), link);

        return new SendMessage(update.message().chat().id(), "Tracking started for the link: " + link);
    }
}
