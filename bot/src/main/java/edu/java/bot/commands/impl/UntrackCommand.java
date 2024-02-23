package edu.java.bot.commands.impl;

import edu.java.bot.commands.CommandType;
import edu.java.bot.commands.CommandWithArguments;

// TODO: разобраться с константными полями
// TODO: сделать бином
public class UntrackCommand implements CommandWithArguments {
    private static final String COMMAND = "/untrack";

    private static final String DESCRIPTION = "Удалить ресурс из списка отслеживаемых.";

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
        return CommandType.UNTRACK;
    }
}
