package edu.java.bot.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import edu.java.bot.entity.dto.AddLinkRequest;
import edu.java.bot.entity.dto.LinkResponse;
import edu.java.bot.entity.dto.ListLinksResponse;
import edu.java.bot.entity.dto.RemoveLinkRequest;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class ScrapperClientTest {
    @RegisterExtension
    public static final WireMockExtension WIRE_MOCK_SERVER = WireMockExtension.newInstance()
        .options(WireMockConfiguration.wireMockConfig().dynamicPort())
        .build();
    private static final String TG_CHAT_CONTROLLER_URI = "/tg-chat/%d";
    private static final String LINK_CONTROLLER_URI = "/links";
    private static final String LINK_CONTROLLER_HEADER = "Tg-Chat-Id";
    private static final Long NON_EXISTING_TG_CHAT_ID = 14L;
    private static final Long EXISTING_TG_CHAT_ID = 123L;

    @DynamicPropertySource
    public static void configureRegistry(DynamicPropertyRegistry registry) {
        registry.add("app.client.scrapper", WIRE_MOCK_SERVER::baseUrl);
    }

    @Autowired
    private ScrapperClient client;

    @Test
    public void assertThatRegisterNewChatReturnedStatusOk() {
        WIRE_MOCK_SERVER.stubFor(WireMock.post(TG_CHAT_CONTROLLER_URI.formatted(NON_EXISTING_TG_CHAT_ID))
            .willReturn(WireMock.ok()));

        ResponseEntity<Void> response = client.registerChat(NON_EXISTING_TG_CHAT_ID).block();

        Objects.requireNonNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void assertThatRegisterExistingChatReturnedStatusConflict() {
        WIRE_MOCK_SERVER.stubFor(WireMock.post(TG_CHAT_CONTROLLER_URI.formatted(EXISTING_TG_CHAT_ID))
            .willReturn(WireMock.status(HttpStatus.CONFLICT.value())));

        ResponseEntity<Void> response = client.registerChat(EXISTING_TG_CHAT_ID).block();

        Objects.requireNonNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void assertThatDeleteExistingChatReturnedStatusOk() {
        WIRE_MOCK_SERVER.stubFor(WireMock.delete(TG_CHAT_CONTROLLER_URI.formatted(EXISTING_TG_CHAT_ID))
            .willReturn(WireMock.ok()));

        ResponseEntity<Void> response = client.deleteChat(EXISTING_TG_CHAT_ID).block();

        Objects.requireNonNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void assertThatDeleteNonExistingChatReturnedStatusNotFound() {
        WIRE_MOCK_SERVER.stubFor(WireMock.delete(TG_CHAT_CONTROLLER_URI.formatted(NON_EXISTING_TG_CHAT_ID))
            .willReturn(WireMock.notFound()));

        ResponseEntity<Void> response = client.deleteChat(NON_EXISTING_TG_CHAT_ID).block();

        Objects.requireNonNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void assertThatGetAllLinksForExistingChatReturnedStatusOk() {
        final ListLinksResponse listLinks =
            new ListLinksResponse(List.of(new LinkResponse(123L, URI.create("https://www.tinkoff.ru/"))), 1);

        WIRE_MOCK_SERVER.stubFor(WireMock.get(LINK_CONTROLLER_URI)
            .withHeader(LINK_CONTROLLER_HEADER, WireMock.equalTo(EXISTING_TG_CHAT_ID.toString()))
            .willReturn(WireMock.ok()
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
                    {
                        "links": [
                            {
                                "id": %d,
                                "url": "%s"
                            }
                        ],
                        "size": %d
                    }
                    """.formatted(
                    listLinks.links().getFirst().id(),
                    listLinks.links().getFirst().url().toString(),
                    listLinks.size()
                ))));

        ResponseEntity<ListLinksResponse> response = client.getAllLinksForChat(EXISTING_TG_CHAT_ID).block();

        Objects.requireNonNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(listLinks, response.getBody());
    }

    @Test
    public void assertThatAddLinkForExistingChatReturnedStatusOk() {
        final Long chatId = 3L;
        final LinkResponse linkResponse = new LinkResponse(2L, URI.create("https://www.tinkoff.ru/"));
        WIRE_MOCK_SERVER.stubFor(WireMock.post(LINK_CONTROLLER_URI)
            .withHeader(LINK_CONTROLLER_HEADER, WireMock.equalTo(chatId.toString()))
            .withRequestBody(WireMock.equalToJson("""
                {
                    "link": "%s"
                }
                """.formatted(linkResponse.url().toString())))
            .willReturn(WireMock.ok()
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
                    {
                        "id": %d,
                        "url": "%s"
                    }
                    """.formatted(linkResponse.id(), linkResponse.url().toString()))));

        ResponseEntity<LinkResponse> response = client.addLink(chatId, new AddLinkRequest(linkResponse.url())).block();

        Objects.requireNonNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(linkResponse, response.getBody());
    }

    @Test
    public void assertThatAddAlreadyAddedLinkForExistingChatReturnedStatusOk() {
        final Long chatId = 4L;
        final AddLinkRequest request = new AddLinkRequest(URI.create("https://www.tinkoff.ru/"));
        WIRE_MOCK_SERVER.stubFor(WireMock.post(LINK_CONTROLLER_URI)
            .withHeader(LINK_CONTROLLER_HEADER, WireMock.equalTo(chatId.toString()))
            .withRequestBody(WireMock.equalToJson("""
                {
                    "link": "%s"
                }
                """.formatted(request.link().toString())))
            .willReturn(WireMock.badRequest()));

        ResponseEntity<LinkResponse> response = client.addLink(chatId, request).block();

        Objects.requireNonNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void assertThatDeleteExistingLinkReturnedStatusOk() {
        final Long chatId = 3L;
        final RemoveLinkRequest request = new RemoveLinkRequest(URI.create("https://www.tinkoff.ru/"));
        final Long linkId = 1L;
        WIRE_MOCK_SERVER.stubFor(WireMock.delete(LINK_CONTROLLER_URI)
            .withHeader(LINK_CONTROLLER_HEADER, WireMock.equalTo(chatId.toString()))
            .withRequestBody(WireMock.equalToJson("""
                {
                    "link": "%s"
                }
                """.formatted(request.link().toString())))
            .willReturn(WireMock.ok()
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
                    {
                        "id": %d,
                        "url": "%s"
                    }
                    """.formatted(linkId, request.link()))));

        ResponseEntity<LinkResponse> response = client.removeLink(chatId, request).block();

        Objects.requireNonNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new LinkResponse(linkId, request.link()), response.getBody());
    }

    @Test
    public void assertThatDeleteNonExistingLinkReturnedStatusNotFound() {
        final Long chatId = 4L;
        final RemoveLinkRequest request = new RemoveLinkRequest(URI.create("https://www.tinkoff.ru/"));
        WIRE_MOCK_SERVER.stubFor(WireMock.delete(LINK_CONTROLLER_URI)
            .withHeader(LINK_CONTROLLER_HEADER, WireMock.equalTo(chatId.toString()))
            .withRequestBody(WireMock.equalToJson("""
                {
                    "link": "%s"
                }
                """.formatted(request.link().toString())))
            .willReturn(WireMock.notFound()));

        ResponseEntity<LinkResponse> response = client.removeLink(chatId, request).block();

        Objects.requireNonNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
