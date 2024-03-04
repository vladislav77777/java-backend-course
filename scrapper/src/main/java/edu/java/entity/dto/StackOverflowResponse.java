package edu.java.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StackOverflowResponse(@JsonProperty("items") List<ItemResponse> items) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ItemResponse(
        @JsonProperty("owner") Owner owner,
        @JsonProperty("last_activity_date") long lastActivityDate,
        @JsonProperty("creation_date") long creationDate,
        @JsonProperty("answer_id") long answerId,
        @JsonProperty("body") String body
    ) {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Owner(
            @JsonProperty("account_id") long accountId
        ) {
        }

    }

}

