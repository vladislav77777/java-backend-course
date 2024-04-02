package edu.java.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,
    LinkClient client,
    DebugInformation debugInformation,
    AccessType databaseAccessType
) {
    public record Scheduler(
        boolean enable,
        @NotNull Duration interval,
        @NotNull Duration forceCheckDelay,
        @NotNull Duration linkLastCheckInterval) {
    }

    public record LinkClient(String gitHub, String stackOverflow, String bot) {
    }

    public record DebugInformation(Integer apiErrorResponseStackTraceListLength) {
    }

    public enum AccessType {
        JDBC, JPA, JOOQ
    }
}
