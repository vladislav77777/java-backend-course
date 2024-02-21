package edu.java.client;

import edu.java.dto.GitHubResponse;
import java.util.Objects;

public class GitHubClient extends Client {

    public GitHubClient(String url) {
        super(url);
    }

    public GitHubResponse getIssue(String owner, String repo, long issueNumber) {
        return Objects.requireNonNull(webClient.get()
            .uri("/repos/{owner}/{repo}/issues/{issueNumber}", owner, repo, issueNumber)
            .retrieve()
            .bodyToMono(GitHubResponse.class)
            .block());
    }
}
