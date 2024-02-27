package edu.java.bot.repository;

import edu.java.bot.entity.UserChat;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class LinkTrackerTest {
    @Test
    public void assertThatRepositoryWorksRight() {
        LinkTracker repository = new LinkTracker();
        long chatId = 123L;
        List<String> trackingLinks = List.of();

        assertNull(repository.findById(chatId));

        UserChat userChat = new UserChat(chatId, trackingLinks);
        repository.add(userChat);

        assertSame(userChat, repository.findById(chatId));
    }
}
