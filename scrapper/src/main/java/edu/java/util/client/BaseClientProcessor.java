package edu.java.util.client;

import edu.java.entity.Link;
import java.net.URI;
import reactor.core.publisher.Mono;

public abstract class BaseClientProcessor {
    protected final String host;

    public BaseClientProcessor(String host) {
        this.host = host;
    }

    public abstract boolean isCandidate(URI link);

    public abstract Mono<String> getUpdate(Link link);
}
