package edu.java.client;

import edu.java.entity.dto.GitHubResponse;
import java.util.List;
import java.util.Objects;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.retry.support.RetryTemplate;
import reactor.core.publisher.Mono;

public class GitHubClient extends Client {
    private final RetryTemplate retryTemplate;

    public GitHubClient(String baseUrl, RetryTemplate retryTemplate) {
        super(baseUrl);

        this.retryTemplate = retryTemplate;
    }

    public Mono<List<GitHubResponse>> getIssue(String owner, String repo, long issueNumber) {
        return retryTemplate.execute(context -> Objects.requireNonNull(webClient.get()
            .uri("/repos/{owner}/{repo}/issues/{issueNumber}/comments", owner, repo, issueNumber)
//            .header(
//                "Authorization",
//                "token " + "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"
//            ) //  Authorization с токеном доступа
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<GitHubResponse>>() {
            })));
    }
}
