package edu.java.exception;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class LinkAlreadyTrackingException extends ResponseStatusException {
    public LinkAlreadyTrackingException(Long tgChatId, URI url) {
        super(HttpStatus.BAD_REQUEST, "Link %s is already tracking by id %d".formatted(url.toString(), tgChatId));
    }
}
