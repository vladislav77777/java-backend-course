package edu.java.controller;

import edu.java.client.GitHubClient;
import edu.java.entity.dto.GitHubResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Check http://localhost:8080/github/issue/Spiderpig86/Cirrus/215
@RestController
@RequestMapping("/github")
public class GitHubController {

    private final GitHubClient gitHubIssueClient;

    @Autowired
    public GitHubController(GitHubClient gitHubIssueClient) {
        this.gitHubIssueClient = gitHubIssueClient;
    }

    @GetMapping("/issue/{owner}/{repo}/{issueNumber}")
    public GitHubResponse getGitHubIssue(
        @PathVariable String owner,
        @PathVariable String repo,
        @PathVariable long issueNumber
    ) {
        return gitHubIssueClient.getIssue(owner, repo, issueNumber).block();
    }
}

