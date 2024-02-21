package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.client.StackOverflowClient;
import edu.java.dto.StackOverflowResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StackOverflowClientTest {
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
    public void testFetchQuestion() {
        // Simulate response from Stack Overflow API
        stubFor(get(urlEqualTo("/questions/1/answers?site=stackoverflow&filter=withbody"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(
                    "{ \"items\": [{\"owner\": {\"account_id\": 123}, \"last_activity_date\": 1645718400, \"creation_date\": 1645718400, \"answer_id\": 456, \"body\": \"This is the answer.\"}]}")));

        // Use the client with WireMock
        StackOverflowClient stackOverflowClient = new StackOverflowClient("http://localhost:8080");
        // Getting Reply from Customer
        StackOverflowResponse response = stackOverflowClient.fetchQuestion(1);

        // Check that the response contains the expected data
        assertEquals(1, response.items().size());
        StackOverflowResponse.ItemResponse item = response.items().getFirst();
        assertEquals(123, item.owner().accountId());
        assertEquals(1645718400, item.lastActivityDate());
        assertEquals(1645718400, item.creationDate());
        assertEquals(456, item.answerId());
        assertEquals("This is the answer.", item.body());
    }

    @Test
    public void testFetchNonexistentQuestion() {
        // Simulate a response from Stack Overflow API when querying a nonexistent question
        stubFor(get(urlEqualTo("/questions/999/answers?site=stackoverflow&filter=withbody"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody("{ \"message\": \"Not Found\" }")));

        // Use the client with WireMock
        StackOverflowClient stackOverflowClient = new StackOverflowClient("http://localhost:8080");

        // Check that when requesting a non-existent question, WebClientResponseException with 404 code is discarded
        assertThrows(WebClientResponseException.class, () -> stackOverflowClient.fetchQuestion(999));
    }
}
