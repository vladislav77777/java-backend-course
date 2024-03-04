package edu.java.bot.command;

import edu.java.bot.entity.dto.ApiErrorResponse;
import edu.java.bot.entity.dto.LinkResponse;
import edu.java.bot.exception.ApiErrorResponseException;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UntrackCommandTest extends CommandTest {
    @InjectMocks
    private UntrackCommand unTrackCommand;

    @Override
    public void init() {
        super.init();
        unTrackCommand = new UntrackCommand(client);
    }

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
        Mockito.doReturn("/untrack https://www.tinkoff.ru").when(message).text();
        URI uri = Mockito.spy(URI.create("https://www.tinkoff.ru"));
        Mockito.doReturn(Mono.just(ResponseEntity.ok().body(new LinkResponse(0L, uri)))).when(client)
            .removeLink(Mockito.any(), Mockito.any());
        assertEquals(
            "Tracking stopped for the link: https://www.tinkoff.ru",
            unTrackCommand.handle(update).getParameters().get("text")
        );
    }

    @Test
    public void assertThatUnTrackNotExistingLinkReturnedRightResponse() {
        when(message.text()).thenReturn("/untrack https://www.tinkoff.ru");

        ApiErrorResponse mock = Mockito.mock(ApiErrorResponse.class);
        Mockito.doReturn("Link is not tracked").when(mock).description();
        Mockito.doReturn(Mono.error(new ApiErrorResponseException(mock))).when(client)
            .removeLink(Mockito.any(), Mockito.any());
        assertEquals("Link is not tracked", unTrackCommand.handle(update).getParameters().get("text"));
    }
}
