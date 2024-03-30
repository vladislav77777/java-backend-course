package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.client.BotClient;
import edu.java.entity.dto.LinkUpdateRequest;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class BotClientTest {
    private static final String LINK_UPDATES_URI = "/updates";
    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setup() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8090));
        wireMockServer.start();
        WireMock.configureFor("localhost", 8090);
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Autowired
    BotClient client;

    @Test
    public void assertThatSendUpdateReturnedStatusOk() {
        final LinkUpdateRequest request =
            new LinkUpdateRequest(123L, URI.create("https://www.tinkoff.ru/"), "Update", List.of(123L));
        wireMockServer.stubFor(WireMock.post(LINK_UPDATES_URI)
            .withRequestBody(WireMock.equalToJson("""
                {
                    "id": %d,
                    "url": "%s",
                      "description": "%s",
                      "tgChatIds": [
                        %d
                      ]
                }
                """.formatted(request.id(), request.url(), request.description(), request.tgChatIds().getFirst())))
            .willReturn(WireMock.ok()));

        ResponseEntity<Void> response = client.sendUpdate(request);

        Objects.requireNonNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
