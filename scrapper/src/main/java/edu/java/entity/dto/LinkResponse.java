package edu.java.entity.dto;

import java.net.URI;

public record LinkResponse(
    Long id,
    URI url
) {
}
