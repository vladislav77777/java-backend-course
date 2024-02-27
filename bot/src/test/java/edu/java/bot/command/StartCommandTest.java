package edu.java.bot.command;

import edu.java.bot.entity.UserChat;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StartCommandTest extends CommandTest {
    // Class to be tested
    @InjectMocks
    private StartCommand startCommand;

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
        startCommand.handle(update);

        UserChat userChat = repository.findById(chatId);

        assertNotNull(userChat);
        assertEquals(chatId, userChat.getChatId());
        assertEquals(List.of(), userChat.getTrackingLinks());
    }
}
