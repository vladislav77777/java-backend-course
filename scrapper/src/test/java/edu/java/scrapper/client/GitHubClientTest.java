package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.client.GitHubClient;
import edu.java.dto.GitHubResponse;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GitHubClientTest {
    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setup() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8080));
        wireMockServer.start();
        WireMock.configureFor("localhost", 8080);
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testGetIssue() {
        // Simulate response from GitHub API
        stubFor(get(urlEqualTo("/repos/owner/repo/issues/1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{ \"created_at\": \"2023-01-01T12:00:00Z\", \"updated_at\": \"2023-01-02T14:30:00Z\" }")));

        // Use client with WireMock
        GitHubClient gitHubClient = new GitHubClient("http://localhost:8080");
        // Receiving a reply from the customer
        GitHubResponse response = gitHubClient.getIssue("owner", "repo", 1);

        // Check that the response contains the expected data
        assertEquals(OffsetDateTime.parse("2023-01-01T12:00:00Z"), response.getCreatedAt());
        assertEquals(OffsetDateTime.parse("2023-01-02T14:30:00Z"), response.getUpdatedAt());
    }

    @Test
    public void testGetNonexistentIssue() {
        // Simulate response from GitHub API when requesting nonexistent resource
        stubFor(get(urlEqualTo("/repos/owner/repo/issues/999"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody("{ \"message\": \"Not Found\" }")));

        GitHubClient gitHubClient = new GitHubClient("http://localhost:8080");

        // Check that WebClientResponseException with 404 code is discarded when requesting a nonexistent resource
        assertThrows(WebClientResponseException.class, () -> gitHubClient.getIssue("owner", "repo", 999));
    }
}
