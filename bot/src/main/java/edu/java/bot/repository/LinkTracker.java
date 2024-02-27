package edu.java.bot.repository;

import edu.java.bot.entity.UserChat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

/* Temporary tracker-DB */
@Repository
public class LinkTracker {

    // Map to store tracked links for each chat/user
    private Map<Long, UserChat> trackedLinksMap;

    public LinkTracker() {
        trackedLinksMap = new ConcurrentHashMap<>();
    }

    public void add(UserChat userChat) {
        // add the link to the tracked links for the specified chat/user
        trackedLinksMap.put(userChat.getChatId(), userChat);
    }

    public UserChat findById(long chatId) {
        // Return the tracked links for the specified chat/user
        return trackedLinksMap.get(chatId);
    }

//    public static void untrackLink(long chatId, String link) {
//        // remove the link from the tracked links for the specified chat/user
//        trackedLinksMap.computeIfPresent(chatId, (k, v) -> {
//            v.remove(link);
//            return v;
//        });
//    }
}

