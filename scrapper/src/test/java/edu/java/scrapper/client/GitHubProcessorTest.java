package edu.java.scrapper.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
public class GitHubProcessorTest {
    private static final Pattern GIT_HUB_PATH_PATTERN =
        Pattern.compile("^(?<userName>[a-z-A-Z0-9]+)/(?<repositoryName>[\\w-.]+)/issues/(?<issueNumber>[\\d]+)(/)?$");

    @Test
    public void testPattern() {
        String input = "vladislav77777/java-backend-course/issues/5";
        Matcher matcher = GIT_HUB_PATH_PATTERN.matcher(input);
        if (matcher.matches()) {
            System.out.println(matcher.group("userName"));
            System.out.println(matcher.group("repositoryName"));
            System.out.println(matcher.group("issueNumber"));
        } else {
            System.out.println("No match found for input: " + input);
        }
    }
}
