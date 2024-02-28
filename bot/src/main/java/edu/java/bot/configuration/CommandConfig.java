package edu.java.bot.configuration;

import edu.java.bot.command.StartCommand;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.command.UntrackCommand;
import edu.java.bot.repository.LinkTracker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandConfig {

//    @Bean
//    public List<Command> commands(
//        StartCommand startCommand,
//        HelpCommand helpCommand,
//        TrackCommand trackCommand,
//        UntrackCommand untrackCommand,
//        ListCommand listCommand
//    ) {
//        return List.of(startCommand, helpCommand, trackCommand, untrackCommand, listCommand);
//    }

    @Bean
    public StartCommand startCommand(LinkTracker linkTracker) {
        return new StartCommand(linkTracker);
    }

    @Bean
    public TrackCommand trackCommand(LinkTracker linkTracker) {
        return new TrackCommand(linkTracker);
    }

    @Bean
    public UntrackCommand untrackCommand(LinkTracker linkTracker) {
        return new UntrackCommand(linkTracker);
    }

}
