package edu.java.bot.commands.impl;

import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandType;

// TODO: разобраться с константными полями
// TODO: сделать бином
public class HelpCommand implements Command {
    private static final String COMMAND = "/help";

    private static final String DESCRIPTION = "Показать список команд.";

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public CommandType type() {
        return CommandType.HELP;
    }
}
