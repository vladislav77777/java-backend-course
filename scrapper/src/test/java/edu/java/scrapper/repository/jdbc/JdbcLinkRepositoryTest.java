package edu.java.scrapper.repository.jdbc;

import edu.java.entity.Link;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JdbcLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DynamicPropertySource
    public static void setJdbcAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jdbc");
    }

    @Test
    @Transactional
    @Rollback
    public void assertThatAddWorksRight() {
        Link link = new Link()
            .setUrl(URI.create("https://github.com"))
            .setLastUpdatedAt(OffsetDateTime.now());

        assertTrue(jdbcLinkRepository.findAll().isEmpty());
        assertEquals(link.getUrl(), jdbcLinkRepository.add(link).getUrl());
        assertFalse(jdbcLinkRepository.findAll().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void assertThatAddAlreadyExistsEntityThrowsDuplicateKeyException() {
        Link link = new Link().setUrl(URI.create("https://github.com")).setLastUpdatedAt(OffsetDateTime.now());

        jdbcLinkRepository.add(link);
        assertThrows(DuplicateKeyException.class, () -> jdbcLinkRepository.add(link));
    }

    @Test
    @Transactional
    @Rollback
    public void assertThatRemoveWorksRight() {
        assertTrue(jdbcLinkRepository.findAll().isEmpty());

        Link link = jdbcLinkRepository.add(new Link()
            .setUrl(URI.create("https://github.com"))
            .setLastUpdatedAt(OffsetDateTime.now()));

        assertEquals(link.getUrl(), jdbcLinkRepository.remove(link).getUrl());
        assertTrue(jdbcLinkRepository.findAll().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void assertThatRemoveNonExistsEntityThrowsEmptyResultDataAccessException() {
        Link link = new Link()
            .setId(1L)
            .setUrl(URI.create("https://github.com"))
            .setLastUpdatedAt(OffsetDateTime.now());

        assertTrue(jdbcLinkRepository.findAll().isEmpty());
        assertThrows(EmptyResultDataAccessException.class, () -> jdbcLinkRepository.remove(link));
    }

    @Test
    @Transactional
    @Rollback
    public void assertThatFindAllWorksRight() {
        List<Link> links = List.of(
            new Link().setUrl(URI.create("https://link1.ru")).setLastUpdatedAt(OffsetDateTime.now()),
            new Link().setUrl(URI.create("https://link2.ru")).setLastUpdatedAt(OffsetDateTime.now())
        );

        links.forEach(jdbcLinkRepository::add);
        assertEquals(links.size(), jdbcLinkRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    public void assertThatAddChatToLinkAndFindAllForChatWorksRight() {
        final Long chatId = 10L;
        Link link = jdbcLinkRepository.add(new Link()
            .setUrl(URI.create("https://link1.ru"))
            .setLastUpdatedAt(OffsetDateTime.now()));

        jdbcTemplate.update("INSERT INTO telegram_chat (id, registered_at) VALUES (?, NOW())", chatId);
        jdbcLinkRepository.connectChatToLink(chatId, link.getId());
        assertEquals(List.of(link), jdbcLinkRepository.findAllForChat(chatId));
    }
}
