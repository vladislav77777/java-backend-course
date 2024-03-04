package edu.java.bot.command;

import com.pengrad.telegrambot.request.SendMessage;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ListCommandTest extends CommandTest {

    // Class to be tested
    @InjectMocks
    private ListCommand listCommand;

    @Test
    public void assertThatCommandReturnedRightString() {
        assertEquals("/list", listCommand.command());
    }

    @Test
    public void assertThatDescriptionReturnedRightString() {
        assertEquals("Show the list of tracked links", listCommand.description());
    }

    @Test
    @DisplayName("Check /list command")
    void assertThatHandleCommandWithTrackedLinks() {
        List<String> trackedLinks = List.of("https://example.com", "https://example.org");
        when(repository.findById(chatId)).thenReturn(new UserChat(chatId, trackedLinks));

        SendMessage actualResult = listCommand.handle(update);
        String expectedString = expectedResultBuilder();

        assertEquals(expectedString, actualResult.getParameters().get("text"));
        Assertions.assertEquals(chatId, actualResult.getParameters().get("chat_id")
        );
    }

    @Test
    @DisplayName("Check /list command with no links")
    void assertThatHandleCommandWithEmptyTrackedLinks() {
        List<String> trackedLinks = Collections.emptyList();
        when(repository.findById(chatId)).thenReturn(new UserChat(chatId, trackedLinks));

        SendMessage actualResult = listCommand.handle(update);

        assertEquals("The list of tracked links is empty.", actualResult.getParameters().get("text"));
        assertEquals(chatId, actualResult.getParameters().get("chat_id"));
    }

    private String expectedResultBuilder() {
        return String.join("\n", "Tracked Links:", "https://example.com", "https://example.org\n");
    }
}

