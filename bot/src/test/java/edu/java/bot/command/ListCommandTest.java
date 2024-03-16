package edu.java.bot.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.dto.LinkResponse;
import edu.java.bot.entity.dto.ListLinksResponse;
import java.net.URI;
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
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ListCommandTest extends CommandTest {

    // Class to be tested
    @InjectMocks
    private ListCommand listCommand;

    @Override
    public void init() {
        super.init();
        listCommand = new ListCommand(client);
    }

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
        List<LinkResponse> trackedLinks = List.of(
            new LinkResponse(1L, URI.create("https://example.com")),
            new LinkResponse(2L, URI.create("https://example.org"))
        );
        when(client.getAllLinksForChat(chatId)).thenReturn(Mono.just(ResponseEntity.ok()
            .body(new ListLinksResponse(trackedLinks, 2))));

        SendMessage actualResult = listCommand.handle(update);
        String expectedString = expectedResultBuilder();

        assertEquals(expectedString, actualResult.getParameters().get("text"));
        Assertions.assertEquals(chatId, actualResult.getParameters().get("chat_id")
        );
    }

    @Test
    @DisplayName("Check /list command with no links")
    void assertThatHandleCommandWithEmptyTrackedLinks() {
        when(client.getAllLinksForChat(chatId)).thenReturn(Mono.just(ResponseEntity.ok()
            .body(new ListLinksResponse(Collections.emptyList(), 0))));

        SendMessage actualResult = listCommand.handle(update);

        assertEquals("The list of tracked links is empty.", actualResult.getParameters().get("text"));
        assertEquals(chatId, actualResult.getParameters().get("chat_id"));
    }

    private String expectedResultBuilder() {
        return String.join("\n", "Tracked Links:", "1: https://example.com", "2: https://example.org\n");
    }
}

