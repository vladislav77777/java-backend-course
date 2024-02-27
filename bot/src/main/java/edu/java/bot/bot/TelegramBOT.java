package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.command.Command;
import edu.java.bot.command.UserMessageProcessor;
import edu.java.bot.configuration.ApplicationConfig;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(ApplicationConfig.class)
public class TelegramBOT implements Bot {

    private TelegramBot telegramBot;

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBOT.class);

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private UserMessageProcessor messageProcessor;

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        telegramBot.execute(request);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            // each update processing!
            SendMessage response = messageProcessor.process(update);
            // send a reply to the user
            if (response != null) {
                execute(response);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    @PostConstruct
    public void start() {
        this.telegramBot = new TelegramBot(applicationConfig.telegramToken());
        setMenuButtonCommands();
        telegramBot.setUpdatesListener(this);
    }

    private void setMenuButtonCommands() {
        BaseResponse response =
            telegramBot.execute(new SetMyCommands(messageProcessor.commands().stream().map(Command::toApiCommand)
                .toArray(BotCommand[]::new)));
        if (response.isOk()) {
            LOGGER.info("Commands set [MenuButton] successfully");
        } else {
            LOGGER.error("Error setting commands: {}", response.description());
        }
    }

    @Override
    public void close() {
        // освободить ресурсы если необходимо!
    }
}
