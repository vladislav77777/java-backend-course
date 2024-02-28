package edu.java.bot.entity.dto;

import java.net.URI;
import org.jetbrains.annotations.NotNull;

public record AddLinkRequest(
    @NotNull URI link
) {
}
