package edu.java.util.client;

import edu.java.client.GitHubClient;
import edu.java.entity.Link;
import java.net.URI;
import java.time.OffsetDateTime;
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
        if (matcher.matches()) {
            return gitHubClient.getIssue(
                    matcher.group("userName"),
                    matcher.group("repositoryName"),
                    Long.parseLong(matcher.group("issueNumber"))
                )
                .mapNotNull(response -> {

                    System.out.println(response.getUpdatedAt());
                    System.out.println(link.getLastUpdatedAt());
                    System.out.println(OffsetDateTime.now());

                    System.out.println(response.getUpdatedAt().isAfter(link.getLastUpdatedAt()));

                    if (response.getUpdatedAt().isAfter(link.getLastUpdatedAt())) {
                        System.out.println("rep updated");
                        return "Repository updated";
                    }
                    System.out.println("null value");
                    return null;
                });
        }

        return Mono.empty();
    }
}
