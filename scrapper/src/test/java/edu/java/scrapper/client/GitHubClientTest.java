package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import edu.java.client.GitHubClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class GitHubClientTest {

    @RegisterExtension
    public static final WireMockExtension WIRE_MOCK_SERVER = WireMockExtension.newInstance()
        .options(WireMockConfiguration.wireMockConfig().dynamicPort())
        .build();

    @DynamicPropertySource
    private static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("app.client.git-hub", WIRE_MOCK_SERVER::baseUrl);
    }

    @Autowired
    private GitHubClient gitHubClient;

    @Test
    public void testGetNonexistentIssue() {
        // Simulate response from GitHub API when requesting nonexistent resource
        WIRE_MOCK_SERVER.stubFor(get(urlEqualTo("/repos/owner/repo/issues/999/comments"))
            .willReturn(notFound()
                .withHeader("Content-Type", "application/json")
                .withBody("{ \"message\": \"Not Found\" }")));

        // Check that WebClientResponseException with 404 code is thrown when requesting a nonexistent resource
        assertThrows(WebClientResponseException.class, () -> gitHubClient.getIssue("owner", "repo", 999).block());
    }
}
