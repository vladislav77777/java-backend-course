package edu.java.configuration.db;

import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.repository.jpa.JpaTelegramChatRepository;
import edu.java.service.LinkService;
import edu.java.service.TelegramChatService;
import edu.java.service.jpa.JpaLinkService;
import edu.java.service.jpa.JpaTelegramChatService;
import edu.java.util.LinkUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {
    @Bean
    public LinkService linkService(
        JpaLinkRepository jpaLinkRepository,
        JpaTelegramChatRepository jpaTelegramChatRepository,
        LinkUtil linkUtil
    ) {
        return new JpaLinkService(jpaLinkRepository, jpaTelegramChatRepository, linkUtil);
    }

    @Bean
    public TelegramChatService telegramChatService(JpaTelegramChatRepository jpaTelegramChatRepository) {
        return new JpaTelegramChatService(jpaTelegramChatRepository);
    }
}
