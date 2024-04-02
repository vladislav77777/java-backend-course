package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.client.ScrapperClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.mockito.Spy;

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
        Mockito.doReturn(message).when(update).message();
        Mockito.doReturn(chat).when(message).chat();
        Mockito.doReturn(chatId).when(chat).id();
        client = Mockito.spy(new ScrapperClientBuilder().build());
    }
}
