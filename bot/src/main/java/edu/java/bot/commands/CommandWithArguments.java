package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import lombok.NonNull;

public interface CommandWithArguments extends Command {
    @Override
    default String usage() {
        return command() + " link1 link2...";
    }

    @Override
    default boolean supports(@NonNull Update update) {
        messageMustBeNotNull(update);
        return update.message().text().startsWith(command());
    }
}
