package edu.java.bot.repository;

import edu.java.bot.entity.UserChat;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@SpringBootTest
public class LinkTrackerTest {

    @Test
    public void assertThatRepositoryWorksRight() {
        LinkTracker repository = new LinkTrackerImpl();
        long chatId = 123L;
        List<String> trackingLinks = List.of();

        assertNull(repository.findById(chatId));

        UserChat userChat = new UserChat(chatId, trackingLinks);
        repository.save(userChat);

        assertSame(userChat, repository.findById(chatId));
    }
}
