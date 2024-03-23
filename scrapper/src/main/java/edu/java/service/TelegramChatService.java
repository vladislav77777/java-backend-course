package edu.java.service;

import edu.java.entity.dto.ChatOperationResponse;
import org.springframework.transaction.annotation.Transactional;

public interface TelegramChatService {
    @Transactional
    ChatOperationResponse register(Long tgChatId);

    @Transactional
    ChatOperationResponse unregister(Long tgChatId);
}
