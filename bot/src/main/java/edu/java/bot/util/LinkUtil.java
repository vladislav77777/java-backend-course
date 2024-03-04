package edu.java.bot.util;

import java.net.URI;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class LinkUtil {
    private LinkUtil() {
    }

    public static URI parse(String link) {
        try {
            URI uri = new URI(link);
            if (uri.getHost() != null && uri.getPath() != null) {
                return uri;
            }
        } catch (URISyntaxException e) {
            log.error("Failed to parse URI: {}", link, e);
        }
        return null;
    }

}
