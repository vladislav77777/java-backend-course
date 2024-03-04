package edu.java.bot.command;

import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Assertions;
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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StartCommandTest extends CommandTest {
    // Class to be tested
    @InjectMocks
    private StartCommand startCommand;

    @Override
    public void init() {
        super.init();
        startCommand = new StartCommand(client);
    }

    @Test
    public void assertThatCommandReturnedRightString() {
        assertEquals("/start", startCommand.command());
    }

    @Test
    public void assertThatDescriptionReturnedRightString() {
        assertEquals("Register the user", startCommand.description());
    }

    @Test
    public void assertThatNewUserAddInRepository() {

        Mockito.doReturn(Mono.just(ResponseEntity.ok().build())).when(client).registerChat(chatId);
        SendMessage handle = startCommand.handle(update);

        Assertions.assertTrue(handle.getParameters().get("text").toString()
            .contains("You have been successfully registered!"));
    }
}
