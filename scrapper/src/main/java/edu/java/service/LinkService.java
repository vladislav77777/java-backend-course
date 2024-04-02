package edu.java.service;

import edu.java.entity.Link;
import edu.java.entity.dto.LinkResponse;
import edu.java.entity.dto.ListLinksResponse;
import java.net.URI;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface LinkService {
    @Transactional
    LinkResponse add(Long tgChatId, URI url);

    @Transactional
    LinkResponse remove(Long tgChatId, URI url);

    @Transactional(readOnly = true)
    Collection<Link> listAllWithInterval(Duration interval);

    @Transactional(readOnly = true)
    ListLinksResponse listAllForChat(Long tgChatId);

    @Transactional(readOnly = true)
    List<Long> getAllChatsForLink(Long linkId);

    @Transactional
    void updateLink(Link link);
}
