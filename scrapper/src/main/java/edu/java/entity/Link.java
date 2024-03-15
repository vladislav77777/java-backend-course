package edu.java.entity;

import java.net.URI;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
public class Link {
    private Long id;
    private URI url;
}
