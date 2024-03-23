package edu.java.service.jdbc;

import edu.java.entity.Link;
import edu.java.entity.dto.LinkResponse;
import edu.java.entity.dto.ListLinksResponse;
import edu.java.exception.LinkAlreadyTrackingException;
import edu.java.exception.LinkNotSupportedException;
import edu.java.exception.LinkNotTrackingException;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.service.LinkService;
import edu.java.util.LinkUtil;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository jdbcLinkRepository;
    private final LinkUtil linkUtil;

    @Override
    public LinkResponse add(Long tgChatId, URI url) {
        Link link;

        if (!linkUtil.isUrlSupported(url)) {
            throw new LinkNotSupportedException(url);
        }

        try {
            link = jdbcLinkRepository.add(new Link()
                .setUrl(url)
                .setLastUpdatedAt(OffsetDateTime.now()));
        } catch (DuplicateKeyException ignored) {
            link = jdbcLinkRepository.findByUrl(url);
        }

        try {
            jdbcLinkRepository.connectChatToLink(tgChatId, link.getId());
        } catch (DuplicateKeyException ignored) {
            throw new LinkAlreadyTrackingException(tgChatId, url);
        }

        return new LinkResponse(link.getId(), link.getUrl());
    }

    @Override
    public LinkResponse remove(Long tgChatId, URI url) {
        try {
            Link link = jdbcLinkRepository.findByUrl(url);

            jdbcLinkRepository.removeChatToLink(tgChatId, link.getId());

            return new LinkResponse(link.getId(), link.getUrl());
        } catch (EmptyResultDataAccessException ignored) {
            throw new LinkNotTrackingException(tgChatId, url);
        }
    }

    @Override
    public Collection<Link> listAllWithInterval(Duration interval) {
        return jdbcLinkRepository.findAllWithInterval(interval);
    }

    @Override
    public ListLinksResponse listAllForChat(Long tgChatId) {
        Collection<Link> links = jdbcLinkRepository.findAllForChat(tgChatId);

        return new ListLinksResponse(links.stream()
            .map(link -> new LinkResponse(link.getId(), link.getUrl()))
            .toList(), links.size());
    }

    @Override
    public List<Long> getAllChatsForLink(Long linkId) {
        return jdbcLinkRepository.findAllChatsForLink(linkId);
    }

    @Override
    public void updateLink(Link link) {
        jdbcLinkRepository.updateLink(link);
    }

}
