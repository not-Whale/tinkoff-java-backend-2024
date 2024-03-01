package edu.java.bot.commands.impl;

import edu.java.bot.commands.Command;
import edu.java.bot.repositories.user_repository.UserRepository;

public class StartCommand implements Command {
    private static final String COMMAND = "/start";

    private static final String DESCRIPTION = "Запустить бота и зарегистрироваться.";

    private final UserRepository userRepository;

    public StartCommand(UserRepository userRepository) {
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
