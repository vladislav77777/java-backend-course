package edu.java.configuration.db;

import edu.java.repository.jooq.JooqLinkRepository;
import edu.java.repository.jooq.JooqTelegramChatRepository;
import edu.java.service.LinkService;
import edu.java.service.TelegramChatService;
import edu.java.service.jooq.JooqLinkService;
import edu.java.service.jooq.JooqTelegramChatService;
import edu.java.util.LinkUtil;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {
    @Bean
    public JooqLinkRepository jooqLinkRepository(DSLContext dslContext) {
        return new JooqLinkRepository(dslContext);
    }

    @Bean
    public LinkService linkService(JooqLinkRepository jooqLinkRepository, LinkUtil linkUtil) {
        return new JooqLinkService(jooqLinkRepository, linkUtil);
    }

    @Bean
    public JooqTelegramChatRepository jooqTelegramChatRepository(DSLContext dslContext) {
        return new JooqTelegramChatRepository(dslContext);
    }

    @Bean
    public TelegramChatService telegramChatService(JooqTelegramChatRepository jooqTelegramChatRepository) {
        return new JooqTelegramChatService(jooqTelegramChatRepository);
    }

}
