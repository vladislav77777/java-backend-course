package edu.java.util;

import edu.java.util.client.BaseClientProcessor;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkUtil {
    private final List<BaseClientProcessor> clientProcessors;

    public boolean isUrlSupported(URI url) {
        return clientProcessors.stream().anyMatch(processor -> processor.isCandidate(url));
    }
}
