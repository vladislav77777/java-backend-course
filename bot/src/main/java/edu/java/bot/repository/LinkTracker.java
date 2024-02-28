package edu.java.bot.repository;

import edu.java.bot.entity.UserChat;

public interface LinkTracker {
    UserChat findById(Long id);

    void save(UserChat telegramChat);

    void delete(UserChat telegramChat);
}

