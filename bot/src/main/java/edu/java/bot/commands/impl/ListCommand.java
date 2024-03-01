package edu.java.bot.commands.impl;

import edu.java.bot.commands.Command;
import edu.java.bot.repositories.user_repository.UserRepository;

public class ListCommand implements Command {
    private static final String COMMAND = "/list";

    private static final String DESCRIPTION = "Вывести список отслеживаемых ресурсов.";

    private final UserRepository userRepository;

    public ListCommand(UserRepository userRepository) {
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
