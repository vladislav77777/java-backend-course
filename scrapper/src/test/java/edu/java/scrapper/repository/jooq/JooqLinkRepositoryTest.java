package edu.java.scrapper.repository.jooq;

import edu.java.entity.Link;
import edu.java.repository.jooq.JooqLinkRepository;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
public class JooqLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JooqLinkRepository linkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DynamicPropertySource
    public static void setJooqAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jooq");
    }

    @Test
    @Transactional
    @Rollback
    public void assertThatAddWorksRight() {
        final Link link = new Link()
            .setUrl(URI.create("https://github.com"))
            .setLastUpdatedAt(OffsetDateTime.now());

        assertTrue(linkRepository.findAll().isEmpty());
        assertEquals(link.getUrl(), linkRepository.add(link).getUrl());
        assertFalse(linkRepository.findAll().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void assertThatAddAlreadyExistsEntityThrowsDuplicateKeyException() {
        final Link link = new Link()
            .setUrl(URI.create("https://github.com"))
            .setLastUpdatedAt(OffsetDateTime.now());

        linkRepository.add(link);
        assertThrows(DuplicateKeyException.class, () -> linkRepository.add(link));
    }

    @Test
    @Transactional
    @Rollback
    public void assertThatRemoveWorksRight() {
        assertTrue(linkRepository.findAll().isEmpty());

        final Link link = linkRepository.add(new Link()
            .setUrl(URI.create("https://github.com"))
            .setLastUpdatedAt(OffsetDateTime.now()));

        assertEquals(link.getUrl(), linkRepository.remove(link).getUrl());
        assertTrue(linkRepository.findAll().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void assertThatFindAllWorksRight() {
        final List<Link> links = List.of(
            new Link().setUrl(URI.create("https://link1.ru")).setLastUpdatedAt(OffsetDateTime.now()),
            new Link().setUrl(URI.create("https://link2.ru")).setLastUpdatedAt(OffsetDateTime.now())
        );

        links.forEach(linkRepository::add);
        assertEquals(links.size(), linkRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    public void assertThatAddChatToLinkAndFindAllForChatWorksRight() {
        final Long chatId = 10L;
        final Link link = linkRepository.add(new Link()
            .setUrl(URI.create("https://link1.ru"))
            .setLastUpdatedAt(OffsetDateTime.now()));

        jdbcTemplate.update("INSERT INTO telegram_chat (id, registered_at) VALUES (?, NOW())", chatId);
        linkRepository.connectChatToLink(chatId, link.getId());
        assertEquals(List.of(link), linkRepository.findAllForChat(chatId));
    }

    @Test
    @Transactional
    @Rollback
    public void assertThatConnectNonExistsTelegramChatToLinkThrowsException() {
        final Link savedLink = Objects.requireNonNull(linkRepository.add(new Link()
            .setUrl(URI.create("https://link1.ru"))
            .setLastUpdatedAt(OffsetDateTime.now())));

        assertThrows(
            DataIntegrityViolationException.class,
            () -> linkRepository.connectChatToLink(120L, savedLink.getId())
        );
    }

}
