package edu.java.bot.command;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.quality.Strictness.LENIENT;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = LENIENT)
public class HelpCommandTest extends CommandTest {
    @InjectMocks
    private HelpCommand helpCommand;

    @Override
    public void init() {
        super.init();

        helpCommand = new HelpCommand(List.of(
            new StartCommand(client),
            new ListCommand(client),
            new TrackCommand(client),
            new UntrackCommand(client)
        ));
    }

    @Test
    public void assertThatCommandReturnedRightString() {
        assertEquals("/help", helpCommand.command());
    }

    @Test
    public void assertThatDescriptionReturnedRightString() {
        assertEquals("Show the list of available commands", helpCommand.description());
    }

    @Test
    public void assertThatHandleReturnedRightString() {
        String expectedMessage =
            "Available commands:\n/start - Register the user\n/list - Show the list of tracked links\n/track - Start tracking a link\n/untrack - Stop tracking a link\n";

        assertEquals(expectedMessage, helpCommand.handle(update).getParameters().get("text"));
    }
}
