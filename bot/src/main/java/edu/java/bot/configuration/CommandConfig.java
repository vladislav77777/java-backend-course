package edu.java.bot.configuration;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.command.StartCommand;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.command.UntrackCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandConfig {

    @Bean
    public StartCommand startCommand(ScrapperClient client) {
        return new StartCommand(client);
    }

    @Bean
    public TrackCommand trackCommand(ScrapperClient client) {
        return new TrackCommand(client);
    }

    @Bean
    public UntrackCommand untrackCommand(ScrapperClient client) {
        return new UntrackCommand(client);
    }

}
