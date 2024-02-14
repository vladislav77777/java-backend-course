package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class HelpCommand implements Command {

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "Show the list of available commands";
    }

    @Override
    public SendMessage handle(Update update) {
        // Implementation for handling the /help command
        StringBuilder message = new StringBuilder("Available commands:\n");
        // Add descriptions for each command
         message.append("/start - Register the user\n");
         message.append("/list - Show the list of tracked links\n");
         message.append("/track - Start link tracking\n");
         message.append("/untrack - Stop link tracking\n");

        return new SendMessage(update.message().chat().id(), message.toString());
    }
}
