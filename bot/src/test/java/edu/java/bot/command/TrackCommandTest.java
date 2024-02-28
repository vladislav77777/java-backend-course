package edu.java.bot.command;

import edu.java.bot.entity.UserChat;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TrackCommandTest extends CommandTest {

    @InjectMocks
    private TrackCommand trackCommand;

    @Test
    public void assertThatCommandReturnedRightString() {
        assertEquals("/track", trackCommand.command());
    }

    @Test
    public void assertThatDescriptionReturnedRightString() {
        assertEquals("Start tracking a link", trackCommand.description());
    }

    @Test
    public void assertThatWrongSyntaxReturnedRightResponse() {
        when(message.text()).thenReturn("/track");
        assertEquals("Please provide a link to track.", trackCommand.handle(update).getParameters().get("text"));
    }

    @Test
    public void assertThatIncorrectLinkReturnedRightResponse() {
        when(message.text()).thenReturn("/track something_weird");

        assertEquals("Incorrect link", trackCommand.handle(update).getParameters().get("text"));
    }

    @Test
    public void assertThatAlreadyAddedLinkReturnedRightResponse() {
        when(message.text()).thenReturn("/track https://www.tinkoff.ru");
        repository.save(new UserChat(chatId, new ArrayList<>(List.of("https://www.tinkoff.ru"))));

        assertEquals("Link is already tracked", trackCommand.handle(update).getParameters().get("text"));
    }

    @Test
    public void assertThatAddUniqueLinkReturnedRightResponse() {
        Mockito.doReturn("/track https://www.tinkoff.ru").when(message).text();
        repository.save(new UserChat(chatId, new ArrayList<>()));

        assertEquals(
            "Tracking started for the link: " + "https://www.tinkoff.ru",
            trackCommand.handle(update).getParameters().get("text")
        );
    }
}
