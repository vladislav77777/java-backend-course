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
            .links().stream()
            .map(link -> new LinkResponse(link.id(), link.url()))
            .toList();

        return new ListLinksResponse(linksResponses, linksResponses.size());
    }

    public LinkResponse addLinkForChat(Long tgChatId, AddLinkRequest request) {
        TelegramChat telegramChat = getTelegramChatById(tgChatId);
        List<Link> chatLinks = new ArrayList<>(telegramChat.links());

        if (chatLinks.stream().anyMatch(link -> link.url().equals(request.link()))) {
            throw new LinkAlreadyTrackingException(tgChatId, request.link());
        }

        Link link = linkRepository.findByUrl(request.link())
            .orElse(linkRepository.save(new Link()
                .id(new Random().nextLong())
                .url(request.link())));
        chatLinks.add(link);
        telegramChat.links(chatLinks);
        chatRepository.save(telegramChat);

        return new LinkResponse(link.id(), link.url());
    }

    public LinkResponse removeLinkForChat(Long tgChatId, RemoveLinkRequest request) {
        TelegramChat telegramChat = getTelegramChatById(tgChatId);
        ArrayList<Link> chatLinks = new ArrayList<>(telegramChat.links());

        for (Link chatLink : chatLinks) {
            if (chatLink.url().equals(request.link())) {
                chatLinks.remove(chatLink);
                telegramChat.links(chatLinks);
                chatRepository.save(telegramChat);

                return new LinkResponse(chatLink.id(), chatLink.url());
            }
        }

        throw new LinkNotTrackingException(tgChatId, request.link());
    }

    private TelegramChat getTelegramChatById(Long tgChatId) {
        return chatRepository.findById(tgChatId)
            .orElseThrow(() -> new TelegramChatNotExistsException(tgChatId));
    }
}
