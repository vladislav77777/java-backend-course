package edu.java.client;

import edu.java.entity.dto.StackOverflowResponse;
import java.util.Objects;

public class StackOverflowClient extends Client {

    public StackOverflowClient(String url) {
        super(url);
    }

    public StackOverflowResponse fetchQuestion(long questionId) {
        return Objects.requireNonNull(webClient.get()
            .uri(
                "/questions/{questionId}/answers?site=stackoverflow&filter=withbody",
                questionId
            )
            .retrieve()
            .bodyToMono(StackOverflowResponse.class)
            .block());
    }
}
