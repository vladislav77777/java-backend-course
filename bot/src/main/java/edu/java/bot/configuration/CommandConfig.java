package edu.java.bot.configuration;

import edu.java.bot.command.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CommandConfig {

    @Bean
    public List<Command> commands(
        StartCommand startCommand,
        HelpCommand helpCommand,
        TrackCommand trackCommand,
        UntrackCommand untrackCommand,
        ListCommand listCommand
    ) {
        return List.of(startCommand, helpCommand, trackCommand, untrackCommand, listCommand);
    }

    @Bean
    public StartCommand startCommand() {
        return new StartCommand();
    }

    @Bean
    public HelpCommand helpCommand() {
        return new HelpCommand();
    }

    @Bean
    public TrackCommand trackCommand() {
        return new TrackCommand();
    }

    @Bean
    public UntrackCommand untrackCommand() {
        return new UntrackCommand();
    }

}
