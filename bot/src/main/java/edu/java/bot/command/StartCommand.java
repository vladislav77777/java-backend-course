package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.UserChat;
import edu.java.bot.repository.LinkTracker;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StartCommand implements Command {
    private final LinkTracker repository;

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

        if (repository.findById(chat.id()) == null) {
            repository.save(new UserChat(chat.id(), new ArrayList<>()));
        }
        String TEXT_MESSAGE =
            "Welcome! You are now registered. You can view the available commands using the /help command";
        return new SendMessage(update.message().chat().id(), TEXT_MESSAGE);
    }
}
