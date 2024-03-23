package edu.java.controller;

import edu.java.entity.dto.AddLinkRequest;
import edu.java.entity.dto.LinkResponse;
import edu.java.entity.dto.ListLinksResponse;
import edu.java.entity.dto.RemoveLinkRequest;
import edu.java.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
public class LinkController {
    private final LinkService jooqLinkService;

    @GetMapping
    public ListLinksResponse getAllLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId) {
        return jooqLinkService.listAllForChat(tgChatId);
    }

    @PostMapping
    public LinkResponse addLink(@RequestHeader("Tg-Chat-Id") Long tgChatId, @RequestBody AddLinkRequest request) {
        return jooqLinkService.add(tgChatId, request.link());
    }

    @DeleteMapping
    public LinkResponse removeLink(@RequestHeader("Tg-Chat-Id") Long tgChatId, @RequestBody RemoveLinkRequest request) {
        return jooqLinkService.remove(tgChatId, request.link());
    }
}
