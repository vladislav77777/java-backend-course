package edu.java.scrapper.repository.jdbc;

import edu.java.entity.TelegramChat;
import edu.java.repository.jdbc.JdbcTelegramChatRepository;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JdbcTelegramChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcTelegramChatRepository jdbcTelegramChatRepository;

    @DynamicPropertySource
    public static void setJdbcAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jdbc");
    }

    @Test
    @Transactional
    @Rollback
    public void assertThatAddWorksRight() {
        TelegramChat telegramChat = new TelegramChat()
            .setId(123L)
            .setRegisteredAt(OffsetDateTime.now());

        assertTrue(jdbcTelegramChatRepository.findAll().isEmpty());
        jdbcTelegramChatRepository.add(telegramChat);
        assertFalse(jdbcTelegramChatRepository.findAll().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void assertThatAddAlreadyExistsEntityThrowsDuplicateKeyException() {
        TelegramChat telegramChat = new TelegramChat().setId(1L).setRegisteredAt(OffsetDateTime.now());

        jdbcTelegramChatRepository.add(telegramChat);
        assertThrows(DuplicateKeyException.class, () -> jdbcTelegramChatRepository.add(telegramChat));
    }

    @Test
    @Transactional
    @Rollback
    public void assertThatRemoveWorksRight() {
        TelegramChat telegramChat = new TelegramChat()
            .setId(123L)
            .setRegisteredAt(OffsetDateTime.now());

        assertTrue(jdbcTelegramChatRepository.findAll().isEmpty());
        jdbcTelegramChatRepository.add(telegramChat);
        jdbcTelegramChatRepository.remove(telegramChat);
        assertTrue(jdbcTelegramChatRepository.findAll().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void assertThatRemoveNonExistsEntityThrowsEmptyResultDataAccessException() {
        TelegramChat telegramChat = new TelegramChat()
            .setId(123L)
            .setRegisteredAt(OffsetDateTime.now());

        assertTrue(jdbcTelegramChatRepository.findAll().isEmpty());
        assertThrows(EmptyResultDataAccessException.class, () -> jdbcTelegramChatRepository.remove(telegramChat));
    }

    @Test
    @Transactional
    @Rollback
    public void assertThatFindAllWorksRight() {
        List<TelegramChat> chats = List.of(
            new TelegramChat().setId(1L).setRegisteredAt(OffsetDateTime.now()),
            new TelegramChat().setId(2L).setRegisteredAt(OffsetDateTime.now())
        );

        chats.forEach(jdbcTelegramChatRepository::add);
        assertEquals(chats.size(), jdbcTelegramChatRepository.findAll().size());
    }
}
