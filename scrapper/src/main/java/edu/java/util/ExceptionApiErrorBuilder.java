package edu.java.util;

import edu.java.configuration.ApplicationConfig;
import edu.java.entity.dto.ApiErrorResponse;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public final class ExceptionApiErrorBuilder {
    private final ApplicationConfig config;

    public ApiErrorResponse build(ResponseStatusException exception, String exceptionName) {
        return new ApiErrorResponse(
            exception.getReason(),
            Integer.toString(exception.getStatusCode().value()),
            exceptionName,
            exception.getMessage(),
            Arrays.stream(exception.getStackTrace())
                .limit(config.debugInformation().apiErrorResponseStackTraceListLength())
                .map(StackTraceElement::toString)
                .toList()
        );
    }
}
