package edu.java.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubResponse {

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;

    @JsonProperty("body")
    private String body;

    @JsonProperty("user") private GitHubResponse.User user;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record User(
        @JsonProperty("login") String login
    ) {
    }
}
