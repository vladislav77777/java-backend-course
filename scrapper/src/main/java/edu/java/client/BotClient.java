package edu.java.client;

import edu.java.entity.dto.ApiErrorResponse;
import edu.java.entity.dto.LinkUpdateRequest;
import edu.java.exception.ApiErrorResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;

public class BotClient extends Client {

    private final RetryTemplate retryTemplate;

    public BotClient(String baseUrl, RetryTemplate retryTemplate) {
        super(baseUrl);

        this.retryTemplate = retryTemplate;
    }

    public ResponseEntity<Void> sendUpdate(LinkUpdateRequest request) {
//        System.out.println("Heeere");
        return retryTemplate.execute(context -> webClient.post()
            .uri("/updates")
            .bodyValue(request)
            .retrieve()
            .onStatus(
                HttpStatus.BAD_REQUEST::equals,
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiErrorResponseException::new)
            )
            .toBodilessEntity().block());
    }
}
