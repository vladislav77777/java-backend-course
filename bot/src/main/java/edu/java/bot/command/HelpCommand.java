package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HelpCommand implements Command {

    private final List<Command> commands;

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
        return new SendMessage(update.message().chat().id(), getStringBuilder());
    }

    @NotNull private String getStringBuilder() {
        StringBuilder message = new StringBuilder("Available commands:\n");
        // Add descriptions for each command
        commands.forEach(command -> message.append("%s - %s\n".formatted(
            command.command(),
            command.description()
        )));
        return message.toString();
    }
}
