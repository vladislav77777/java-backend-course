package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import edu.java.client.StackOverflowClient;
import edu.java.entity.dto.StackOverflowResponse;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class StackOverflowClientTest {
    @RegisterExtension
    public static final WireMockExtension WIRE_MOCK_SERVER = WireMockExtension.newInstance()
        .options(WireMockConfiguration.wireMockConfig().dynamicPort())
        .build();

    @DynamicPropertySource
    private static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("app.client.stackOverflow", WIRE_MOCK_SERVER::baseUrl);
    }

    @Autowired
    private StackOverflowClient stackOverflowClient;

    @Test
    public void testFetchQuestion() {
        // Simulate response from Stack Overflow API
        WIRE_MOCK_SERVER.stubFor(get(urlEqualTo("/questions/1/answers?site=stackoverflow&filter=withbody"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(
                    "{ \"items\": [{\"owner\": {\"display_name\": \"123\"}, \"last_activity_date\": 1645718400, \"creation_date\": 1645711400, \"answer_id\": 456, \"body\": \"This is the answer.\"}]}")));

        // Use the client with WireMock
        // Getting Reply from Customer
        StackOverflowResponse response = stackOverflowClient.fetchQuestion(1).block();

        // Check that the response contains the expected data
        assertEquals(1, response.items().size());
        StackOverflowResponse.ItemResponse item = response.items().getFirst();
        assertEquals("123", item.owner().displayName());
        assertEquals(
            OffsetDateTime.ofInstant(Instant.ofEpochSecond(1645718400), ZoneId.of("Z")),
            item.lastActivityDate()
        );
        assertEquals(OffsetDateTime.ofInstant(Instant.ofEpochSecond(1645711400), ZoneId.of("Z")), item.creationDate());
        assertEquals(456, item.answerId());
        assertEquals("This is the answer.", item.body());
    }

    @Test
    public void testFetchNonexistentQuestion() {
        // Simulate a response from Stack Overflow API when querying a nonexistent question
        WIRE_MOCK_SERVER.stubFor(get(urlEqualTo("/questions/999?site=stackoverflow&filter=withbody"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody("{ \"message\": \"Not Found\" }")));

        // Use the client with WireMock
        // Check that when requesting a non-existent question, WebClientResponseException with 404 code is discarded
        assertThrows(WebClientResponseException.class, () -> stackOverflowClient.fetchQuestion(999).block());
    }
}
