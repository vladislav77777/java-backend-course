package edu.java.bot.command;

import java.util.ArrayList;
import java.util.List;
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
public class UntrackCommandTest extends CommandTest {
    @InjectMocks
    private UntrackCommand unTrackCommand;

    @Test
    public void assertThatCommandReturnedRightString() {
        assertEquals("/untrack", unTrackCommand.command());
    }

    @Test
    public void assertThatDescriptionReturnedRightString() {
        assertEquals("Stop tracking a link", unTrackCommand.description());
    }

    @Test
    public void assertThatWrongSyntaxReturnedRightResponse() {
        when(message.text()).thenReturn("/untrack");
        assertEquals("Please provide a link to track.", unTrackCommand.handle(update).getParameters().get("text"));
    }

    @Test
    public void assertThatIncorrectLinkReturnedRightResponse() {
        when(message.text()).thenReturn("/untrack some_link");
        assertEquals("Incorrect link", unTrackCommand.handle(update).getParameters().get("text"));
    }

    @Test
    public void assertThatUnTrackExistingLinkReturnedRightResponse() {
        when(message.text()).thenReturn("/untrack https://www.tinkoff.ru");
        repository.save(new UserChat(chatId, new ArrayList<>(List.of("https://www.tinkoff.ru"))));

        assertEquals(
            "Tracking stopped for the link: https://www.tinkoff.ru",
            unTrackCommand.handle(update).getParameters().get("text")
        );
    }

    @Test
    public void assertThatUnTrackNotExistingLinkReturnedRightResponse() {
        when(message.text()).thenReturn("/untrack https://www.tinkoff.ru");
        repository.save(new UserChat(chatId, new ArrayList<>()));

        assertEquals("Link is not tracked", unTrackCommand.handle(update).getParameters().get("text"));
    }
}
