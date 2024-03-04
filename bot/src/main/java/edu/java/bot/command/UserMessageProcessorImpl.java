package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMessageProcessorImpl implements UserMessageProcessor {

    @Autowired
    private final List<Command> commands;
    private String unknownText;

    public UserMessageProcessorImpl(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public List<? extends Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(Update update) {

        String messageText = update.message().text();

        unknownText = "Unknown command. Use /help for command list.";
        if (messageText == null || !messageText.startsWith("/")) {
            // Null command or one that starts not from '/', ignore
            return new SendMessage(
                update.message().chat().id(),
                unknownText
            );
        }

        String commandText = messageText.split(" ")[0]; // Get the command text

        for (Command command : commands) {
            if (command.command().equals(commandText)) {
                // Find the appropriate command, process and return the answer
                return command.handle(update);
            }
        }

        // Unknown team /<command>
        return new SendMessage(
            update.message().chat().id(),
            unknownText
        );
    }
}
