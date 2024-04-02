package edu.java.util.client;

import edu.java.client.StackOverflowClient;
import edu.java.entity.Link;
import edu.java.entity.dto.StackOverflowResponse;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class StackOverflowClientProcessor extends BaseClientProcessor {
    private static final Pattern STACK_OVERFLOW_PATH_PATTERN =
        Pattern.compile("^/questions/(?<questionId>\\d+)(/[\\w-]*)?(/)?$");
    private final StackOverflowClient stackOverflowClient;

    public StackOverflowClientProcessor(StackOverflowClient stackOverflowClient) {
        super("stackoverflow.com");

        this.stackOverflowClient = stackOverflowClient;
    }

    @Override
    public boolean isCandidate(URI url) {
        return host.equals(url.getHost())
            && STACK_OVERFLOW_PATH_PATTERN.matcher(url.getPath()).matches();
    }

    @Override
    public Mono<String> getUpdate(Link link) {
        Matcher matcher = STACK_OVERFLOW_PATH_PATTERN.matcher(link.getUrl().getPath());

        if (matcher.matches()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            return stackOverflowClient.fetchQuestion(Long.parseLong(matcher.group("questionId")))
                .mapNotNull(response -> {
                    StackOverflowResponse.ItemResponse first = response.items().getFirst();
                    if (first.lastActivityDate().isAfter(link.getLastUpdatedAt())) {
                        return "\nQuestion updated " + "by "
                            + first.owner().displayName() + " at "
                            + first.creationDate().format(formatter) + "\n";
                    }

                    return null;
                });
        }

        return Mono.empty();
    }
}
