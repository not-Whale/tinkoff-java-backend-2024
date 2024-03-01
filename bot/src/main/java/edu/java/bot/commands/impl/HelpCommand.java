package edu.java.bot.commands.impl;

import edu.java.bot.commands.Command;
import edu.java.bot.repositories.user_repository.UserRepository;

public class HelpCommand implements Command {
    private static final String COMMAND = "/help";

    private static final String DESCRIPTION = "Показать список команд.";

    private final UserRepository userRepository;

    public HelpCommand(UserRepository userRepository) {
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
