package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class StartCommand implements Command {

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
        return new SendMessage(update.message().chat().id(), "Welcome! You are now registered.");
    }
}
