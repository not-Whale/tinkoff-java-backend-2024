package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import java.util.List;
import lombok.NonNull;

public interface CommandWithArguments extends Command {
    @Override
    default String usage() {
        return command() + " <link1 link2...>";
    }

    @Override
    default boolean supports(@NonNull Update update) {
        messageMustBeNotNull(update);
        return update.message().text().startsWith(command());
    }

    default List<String> getArguments(@NonNull Update update) {
        messageMustBeNotNull(update);
        String[] messageSplit = update.message().text().split("\\s");
        return List.of(messageSplit).subList(1, messageSplit.length);
    }
}
