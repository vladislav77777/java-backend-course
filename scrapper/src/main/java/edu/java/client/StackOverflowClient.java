package edu.java.client;

import edu.java.entity.dto.StackOverflowResponse;
import java.util.Objects;
import reactor.core.publisher.Mono;

public class StackOverflowClient extends Client {

    public StackOverflowClient(String url) {
        super(url);
    }

    public Mono<StackOverflowResponse> fetchQuestion(long questionId) {
        return Objects.requireNonNull(webClient.get()
            .uri(
                "/questions/{questionId}?site=stackoverflow&filter=withbody",
                questionId
            )
            .retrieve()
            .bodyToMono(StackOverflowResponse.class));
    }
}
