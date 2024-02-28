package edu.java.bot.entity.dto;

import java.net.URI;
import org.jetbrains.annotations.NotNull;

public record RemoveLinkRequest(
    @NotNull URI link
) {
}
