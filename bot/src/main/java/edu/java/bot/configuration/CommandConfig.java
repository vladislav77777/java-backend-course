package edu.java.bot.configuration;

import edu.java.bot.command.Command;
import edu.java.bot.command.HelpCommand;
import edu.java.bot.command.ListCommand;
import edu.java.bot.command.StartCommand;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.command.UntrackCommand;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
