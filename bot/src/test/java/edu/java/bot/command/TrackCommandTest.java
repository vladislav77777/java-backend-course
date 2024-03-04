package edu.java.bot.command;

import edu.java.bot.entity.dto.AddLinkRequest;
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
public class TrackCommandTest extends CommandTest {

    @InjectMocks
    private TrackCommand trackCommand;

    @Override
    public void init() {
        super.init();
        trackCommand = new TrackCommand(client);
    }

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
        ApiErrorResponse mock = Mockito.mock(ApiErrorResponse.class);
        Mockito.doReturn("Link is already tracked").when(mock).description();

        Mockito.doReturn(Mono.error(new ApiErrorResponseException(mock)))
            .when(client).addLink(Mockito.any(), Mockito.any());

        assertEquals("Link is already tracked", trackCommand.handle(update).getParameters().get("text"));
    }

    @Test
    public void assertThatAddUniqueLinkReturnedRightResponse() {
        Mockito.doReturn("/track https://www.tinkoff.ru").when(message).text();

        AddLinkRequest request = Mockito.spy(new AddLinkRequest(URI.create("https://www.tinkoff.ru")));

        Mockito.doReturn(Mono.just(ResponseEntity.ok().body(new LinkResponse(0L, request.link()))))
            .when(client).addLink(Mockito.any(), Mockito.any());

        assertEquals(
            "Tracking started for the link: " + "https://www.tinkoff.ru",
            trackCommand.handle(update).getParameters().get("text")
        );

    }
}
