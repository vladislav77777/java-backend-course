package edu.java.service;

import edu.java.entity.TelegramChat;
import edu.java.entity.dto.ChatOperationResponse;
import edu.java.exception.TelegramChatAlreadyRegistered;
import edu.java.exception.TelegramChatNotExistsException;
import edu.java.repository.TelegramChatRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@RequiredArgsConstructor
public class TelegramChatService {
    private final TelegramChatRepository chatRepository;

    public ChatOperationResponse registerChat(@PathVariable Long id) {
        chatRepository.findById(id).ifPresent(ignore -> {
            throw new TelegramChatAlreadyRegistered(id);
        });

        TelegramChat savedTelegramChat = chatRepository.save(new TelegramChat()
            .id(id)
            .links(new ArrayList<>()));

        return new ChatOperationResponse(savedTelegramChat != null);
    }

    public ChatOperationResponse deleteChat(@PathVariable Long id) {
        chatRepository.delete(chatRepository.findById(id)
            .orElseThrow(() -> new TelegramChatNotExistsException(id)));

        return new ChatOperationResponse(true);
    }

}
