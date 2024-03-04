package edu.java.util;

import edu.java.entity.dto.ApiErrorResponse;
import java.util.Arrays;
import org.springframework.web.server.ResponseStatusException;

public final class ExceptionApiErrorBuilder {
    private ExceptionApiErrorBuilder() {
    }

    public static ApiErrorResponse build(ResponseStatusException exception, String exceptionName) {
        return new ApiErrorResponse(
            exception.getReason(),
            Integer.toString(exception.getStatusCode().value()),
            exceptionName,
            exception.getMessage(),
            Arrays.stream(exception.getStackTrace())
                .map(StackTraceElement::toString)
                .toList()
        );
    }
}
