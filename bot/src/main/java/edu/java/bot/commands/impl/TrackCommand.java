package edu.java.bot.commands.impl;

import edu.java.bot.commands.CommandWithArguments;
import edu.java.bot.repositories.user_repository.UserRepository;

public class TrackCommand implements CommandWithArguments {
    private static final String COMMAND = "/track";

    private static final String DESCRIPTION = "Добавить ресурс в список отслеживаемых.";

    private final UserRepository userRepository;

    public TrackCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }
}
