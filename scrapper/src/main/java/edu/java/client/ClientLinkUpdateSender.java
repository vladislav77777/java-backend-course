package edu.java.client;

import edu.java.entity.dto.LinkUpdateRequest;
import edu.java.service.LinkUpdateService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientLinkUpdateSender implements LinkUpdateService {
    private final BotClient botClient;

    @Override
    public void sendUpdate(LinkUpdateRequest request) {
        botClient.sendUpdate(request);
    }
}
