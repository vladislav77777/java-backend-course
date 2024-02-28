package edu.java.bot.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChat {
    private Long chatId;
    private List<String> trackingLinks;
}
