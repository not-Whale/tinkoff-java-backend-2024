package edu.java.bot.commands.impl;

import edu.java.bot.commands.CommandType;
import edu.java.bot.commands.CommandWithArguments;

// TODO: разобраться с константными полями
// TODO: сделать бином
public class TrackCommand implements CommandWithArguments {
    private static final String COMMAND = "/track";

    private static final String DESCRIPTION = "Добавить ресурс в список отслеживаемых.";

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
        return CommandType.TRACK;
    }
}
