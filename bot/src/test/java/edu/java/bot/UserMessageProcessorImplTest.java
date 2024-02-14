package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.command.UserMessageProcessorImpl;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserMessageProcessorImplTest {

    @InjectMocks
    private UserMessageProcessorImpl messageProcessor;

    @Mock
    private Command knownCommand;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    static Stream<String> unknownCommands() {
        return Stream.of("/unknown", "/unrecognized", "/stats");
    }

    static Stream<String> illegalCommands() {
        return Stream.of("", null, "something");
    }

    @ParameterizedTest
    @MethodSource("unknownCommands")
    @DisplayName("Check processing unknown commands")
    void processUnknownCommand(String unknownCommandText) {
        // Set up the mock objects and data
        setUpMock(unknownCommandText);
        mockCommandReturning();

        // Execute the method under test
        SendMessage result = messageProcessor.process(update);

        // Verify the expected behavior
        Assertions.assertEquals("Unknown command. Use /help for command list.", result.getParameters().get("text"));
        Assertions.assertEquals(123L, result.getParameters().get("chat_id"));
    }

    @ParameterizedTest
    @MethodSource("illegalCommands")
    @DisplayName("Check processing illegal commands")
    void processIllegalCommand(String illegalCommandText) {
        // Set up the mock objects and data
        setUpMock(illegalCommandText);

        // Execute the method under test
        SendMessage result = messageProcessor.process(update);

        // Verify the expected behavior
        Assertions.assertEquals("Unknown command. Use /help for command list.", result.getParameters().get("text"));
        Assertions.assertEquals(123L, result.getParameters().get("chat_id"));
    }

    private void mockCommandReturning() {
        // Simulate having only the knownCommand in the list of commands
        List<Command> commands = Collections.singletonList(knownCommand);
        messageProcessor = new UserMessageProcessorImpl(commands);
        // Mock the behavior of the known command
        when(knownCommand.command()).thenReturn("/known");
    }

    private void setUpMock(String unknownCommandText) {
        // Mocking chat_id = 123
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn(unknownCommandText);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123L);
    }

}
