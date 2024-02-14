package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.LinkTracker;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListCommandTest {

    // Class to be tested
    @InjectMocks
    private ListCommand listCommand;

    // Dependencies (will be mocked)
    @Mock
    private LinkTracker linkTracker;
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;

    final long chatId = 123L;

    @BeforeEach
    void init() {
//        === when(update.message().chat().id()).thenReturn(chatId);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
    }

    @Test
    @DisplayName("Check /list command")
    void handleCommandWithTrackedLinks() {
        List<String> trackedLinks = List.of("https://example.com", "https://example.org");
        when(linkTracker.getTrackedLinks(chatId)).thenReturn(trackedLinks);

        SendMessage actualResult = listCommand.handle(update);
        String expectedString = expectedResultBuilder();

        Assertions.assertEquals(expectedString, actualResult.getParameters().get("text"));
        Assertions.assertEquals(chatId, actualResult.getParameters().get("chat_id")
        );
    }

    @Test
    @DisplayName("Check /list command with no links")
    void handleCommandWithEmptyTrackedLinks() {
        List<String> trackedLinks = Collections.emptyList();
        when(linkTracker.getTrackedLinks(chatId)).thenReturn(trackedLinks);

        SendMessage actualResult = listCommand.handle(update);

        Assertions.assertEquals("The list of tracked links is empty.", actualResult.getParameters().get("text"));
        Assertions.assertEquals(chatId, actualResult.getParameters().get("chat_id"));
    }

    private String expectedResultBuilder() {
        return String.join("\n", "Tracked Links:", "https://example.com", "https://example.org\n");
    }
}

