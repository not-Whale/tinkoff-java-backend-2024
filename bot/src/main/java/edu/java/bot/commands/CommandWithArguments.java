package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import java.util.Arrays;

public interface CommandWithArguments extends Command {
    @Override
    default boolean isUpdateContainsCommand(Update update) {
        return update.message().text().startsWith(command());
    }

    default String[] arguments(Update update) {
        String[] commandSplit = update.message().text().split("\\s");
        return Arrays.copyOfRange(commandSplit, 1, commandSplit.length);
    }
}
