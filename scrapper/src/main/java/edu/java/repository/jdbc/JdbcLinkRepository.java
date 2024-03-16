package edu.java.repository.jdbc;

import edu.java.entity.Link;
import java.net.URI;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements JdbcRepository<Link> {
    private static final String ADD_QUERY = "INSERT INTO link (url, last_updated_at) VALUES (?, ?) RETURNING *";
    private static final String DELETE_QUERY = "DELETE FROM link WHERE id=? RETURNING *";
    private static final String SELECT_ALL = "SELECT * FROM link";
    private static final String SELECT_ALL_WITH_INTERVAL = "SELECT * FROM link WHERE last_updated_at < ?";
    private static final String SELECT_BY_URL = "SELECT * FROM link WHERE url=?";
    private static final String ADD_CHAT_TO_LINK =
        "INSERT INTO assignment (chat_id, link_id) VALUES (?, ?)";
    private static final String DELETE_CHAT_TO_LINK = "DELETE FROM assignment WHERE chat_id=? AND link_id=?";
    private static final String SELECT_ALL_FOR_CHAT =
        "SELECT * FROM link INNER JOIN assignment a on link.id = a.link_id WHERE a.chat_id=?";
    private static final String SELECT_ALL_CHATS_FOR_LINK = "SELECT chat_id FROM assignment WHERE link_id=?";
    private static final String UPDATE_LINK = "UPDATE link SET last_updated_at=? WHERE id=?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public Link add(Link entity) {
        return jdbcTemplate.queryForObject(
            ADD_QUERY,
            new BeanPropertyRowMapper<>(Link.class),
            entity.getUrl().toString(),
            entity.getLastUpdatedAt()
        );
    }

    @Override
    @Transactional
    public Link remove(Link entity) {
        return jdbcTemplate.queryForObject(DELETE_QUERY, new BeanPropertyRowMapper<>(Link.class), entity.getId());
    }

    @Override
    @Transactional
    public Collection<Link> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new BeanPropertyRowMapper<>(Link.class));
    }

    @Transactional
    public Collection<Link> findAllWithInterval(Duration interval) {
        return jdbcTemplate.query(
            SELECT_ALL_WITH_INTERVAL,
            new BeanPropertyRowMapper<>(Link.class),
            Timestamp.from(OffsetDateTime.now().minusSeconds(interval.getSeconds()).toInstant())
        );
    }

    @Transactional
    public Link findByUrl(URI url) {
        return jdbcTemplate.queryForObject(
            SELECT_BY_URL,
            new BeanPropertyRowMapper<>(Link.class),
            url.toString()
        );
    }

    @Transactional
    public void connectChatToLink(Long chatId, Long linkId) {
        jdbcTemplate.update(ADD_CHAT_TO_LINK, chatId, linkId);
    }

    @Transactional
    public void removeChatToLink(Long chatId, Long linkId) {
        jdbcTemplate.update(DELETE_CHAT_TO_LINK, chatId, linkId);
    }

    @Transactional
    public Collection<Link> findAllForChat(Long chatId) {
        return jdbcTemplate.query(SELECT_ALL_FOR_CHAT, new BeanPropertyRowMapper<>(Link.class), chatId);
    }

    @Transactional
    public List<Long> findAllChatsForLink(Long linkId) {
        return jdbcTemplate.query(
            SELECT_ALL_CHATS_FOR_LINK,
            (rs, rowNum) -> rs.getLong("chat_id"),
            linkId
        );
    }

    @Transactional
    public void updateLink(Link link) {
        jdbcTemplate.update(UPDATE_LINK, link.getLastUpdatedAt(), link.getId());
    }
}
