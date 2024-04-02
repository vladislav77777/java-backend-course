package edu.java.client;

import edu.java.entity.dto.StackOverflowResponse;
import java.util.Objects;
import org.springframework.retry.support.RetryTemplate;
import reactor.core.publisher.Mono;

public class StackOverflowClient extends Client {
    private final RetryTemplate retryTemplate;

    public StackOverflowClient(String baseUrl, RetryTemplate retryTemplate) {
        super(baseUrl);

        this.retryTemplate = retryTemplate;
    }


    public Mono<StackOverflowResponse> fetchQuestion(long questionId) {
        return retryTemplate.execute(context -> Objects.requireNonNull(webClient.get()
            .uri(
                "/questions/{questionId}/answers?site=stackoverflow&filter=withbody",
                questionId
            )
            .retrieve()
            .bodyToMono(StackOverflowResponse.class)));
    }

}
