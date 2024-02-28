package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.UserChat;
import edu.java.bot.repository.LinkTracker;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HelpCommand implements Command {

    private final List<Command> commands;

    private final LinkTracker repository;

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
        commands.forEach(command -> message.append("%s - %s\n".formatted(
            command.command(),
            command.description()
        )));
//         message.append("/start - Register the user\n");
//         message.append("/list - Show the list of tracked links\n");
//         message.append("/track - Start link tracking\n");
//         message.append("/untrack - Stop link tracking\n");
        long chatId = update.message().chat().id();
        UserChat userChat = repository.findById(chatId);
//        if (userChat == null) {
//            return new SendMessage(chatId, "Please, register -- /start.");
//        }
        return new SendMessage(update.message().chat().id(), message.toString());
    }
}
