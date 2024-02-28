package edu.java.service;

import edu.java.entity.Link;
import edu.java.entity.TelegramChat;
import edu.java.entity.dto.AddLinkRequest;
import edu.java.entity.dto.LinkResponse;
import edu.java.entity.dto.ListLinksResponse;
import edu.java.entity.dto.RemoveLinkRequest;
import edu.java.exception.LinkAlreadyTrackingException;
import edu.java.exception.LinkNotTrackingException;
import edu.java.exception.TelegramChatNotExistsException;
import edu.java.repository.LinkRepository;
import edu.java.repository.TelegramChatRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkService {
    private final LinkRepository linkRepository;
    private final TelegramChatRepository chatRepository;

    public ListLinksResponse getAllLinksForChat(Long tgChatId) {
        List<LinkResponse> linksResponses = getTelegramChatById(tgChatId)
            .getLinks().stream()
            .map(link -> new LinkResponse(link.getId(), link.getUrl()))
            .toList();

        return new ListLinksResponse(linksResponses, linksResponses.size());
    }

    public LinkResponse addLinkForChat(Long tgChatId, AddLinkRequest request) {
        TelegramChat telegramChat = getTelegramChatById(tgChatId);
        List<Link> chatLinks = new ArrayList<>(telegramChat.getLinks());

        if (chatLinks.stream().anyMatch(link -> link.getUrl().equals(request.link()))) {
            throw new LinkAlreadyTrackingException(tgChatId, request.link());
        }

        Link link = linkRepository.findByUrl(request.link())
            .orElse(linkRepository.save(new Link(new Random().nextLong(), request.link())));
        chatLinks.add(link);
        telegramChat.setLinks(chatLinks);
        chatRepository.save(telegramChat);

        return new LinkResponse(link.getId(), link.getUrl());
    }

    public LinkResponse removeLinkForChat(Long tgChatId, RemoveLinkRequest request) {
        TelegramChat telegramChat = getTelegramChatById(tgChatId);
        ArrayList<Link> chatLinks = new ArrayList<>(telegramChat.getLinks());

        for (Link chatLink : chatLinks) {
            if (chatLink.getUrl().equals(request.link())) {
                chatLinks.remove(chatLink);
                telegramChat.setLinks(chatLinks);
                chatRepository.save(telegramChat);

                return new LinkResponse(chatLink.getId(), chatLink.getUrl());
            }
        }

        throw new LinkNotTrackingException(tgChatId, request.link());
    }

    private TelegramChat getTelegramChatById(Long tgChatId) {
        return chatRepository.findById(tgChatId)
            .orElseThrow(() -> new TelegramChatNotExistsException(tgChatId));
    }
}
