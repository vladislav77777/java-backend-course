package edu.java.util.client;

import edu.java.client.GitHubClient;
import edu.java.entity.Link;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GitHubClientProcessor extends BaseClientProcessor {
    private static final Pattern GIT_HUB_PATH_PATTERN =
        Pattern.compile("^/(?<userName>[a-z-A-Z0-9]+)/(?<repositoryName>[\\w-.]+)/issues/(?<issueNumber>[\\d]+)(/)?$");
    private final GitHubClient gitHubClient;

    public GitHubClientProcessor(GitHubClient gitHubClient) {
        super("github.com");

        this.gitHubClient = gitHubClient;
    }

    @Override
    public boolean isCandidate(URI url) {
        return host.equals(url.getHost().toLowerCase())
            && GIT_HUB_PATH_PATTERN.matcher(url.getPath()).matches();
    }

    @Override
    public Mono<String> getUpdate(Link link) {
        Matcher matcher = GIT_HUB_PATH_PATTERN.matcher(link.getUrl().getPath());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (matcher.matches()) {
            return gitHubClient.getIssue(
                    matcher.group("userName"),
                    matcher.group("repositoryName"),
                    Long.parseLong(matcher.group("issueNumber"))
                )
                .mapNotNull(response -> {
                    if (response.getLast().getUpdatedAt().isAfter(link.getLastUpdatedAt())) {

                        return "\nRepository updated " + "by "
                            + response.getLast().getUser().login() + " at "
                            + response.getLast().getUpdatedAt().format(formatter) + "\nMessage: "
                            + response.getLast().getBody();
                    }

                    return null;
                });
        }

        return Mono.empty();
    }
}
