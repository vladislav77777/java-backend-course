package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.client.ScrapperClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.stereotype.Component;
import static org.mockito.Mockito.when;

@Component
public abstract class CommandTest {
    protected final long chatId = 123L;
    // Dependencies (will be mocked)
    protected ScrapperClient client;

    @Spy protected Update update;
    @Spy protected Message message;
    @Spy
    private Chat chat;

    @BeforeEach
    void init() {
//        === when(update.message().chat().id()).thenReturn(chatId);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        client = Mockito.spy(new ScrapperClientBuilder().build());
    }
}
