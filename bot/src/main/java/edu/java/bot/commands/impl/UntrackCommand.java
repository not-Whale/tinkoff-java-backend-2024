package edu.java.bot.commands.impl;

import edu.java.bot.commands.CommandWithArguments;
import edu.java.bot.repositories.user_repository.UserRepository;

public class UntrackCommand implements CommandWithArguments {
    private static final String COMMAND = "/untrack";

    private static final String DESCRIPTION = "Удалить ресурс из списка отслеживаемых.";

    private final UserRepository userRepository;

    public UntrackCommand(UserRepository userRepository) {
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
