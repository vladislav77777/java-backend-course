package edu.java.entity;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Link {
    private Long id;
    private URI url;
    private OffsetDateTime lastUpdatedAt;
}
