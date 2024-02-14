package edu.java.bot;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* Temporary tracker-DB */
@Component
public class LinkTracker {

    // Map to store tracked links for each chat/user
    private static Map<Long, List<String>> trackedLinksMap;

    public LinkTracker() {
        trackedLinksMap = new HashMap<>();
    }

    public static void trackLink(long chatId, String link) {
        // add the link to the tracked links for the specified chat/user
        trackedLinksMap.computeIfAbsent(chatId, k -> new ArrayList<>()).add(link);
    }

    public List<String> getTrackedLinks(long chatId) {
        // Return the tracked links for the specified chat/user
        return trackedLinksMap.getOrDefault(chatId, List.of());
    }

    public static void untrackLink(long chatId, String link) {
        // remove the link from the tracked links for the specified chat/user
        trackedLinksMap.computeIfPresent(chatId, (k, v) -> {
            v.remove(link);
            return v;
        });
    }
}

