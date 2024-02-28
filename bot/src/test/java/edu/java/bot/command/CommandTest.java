package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.repository.LinkTrackerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Spy;
import static org.mockito.Mockito.when;

public abstract class CommandTest {
    protected final long chatId = 123L;
    // Dependencies (will be mocked)
    @Spy protected LinkTrackerImpl repository;
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
    }
}
