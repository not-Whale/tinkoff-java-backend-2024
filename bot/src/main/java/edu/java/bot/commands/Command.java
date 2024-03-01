package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exceptions.NullMessageException;
import lombok.NonNull;

public interface Command {
    String command();

    String description();

    SendMessage process(Update update);

    default String usage() {
        return command();
    }

    default void messageMustBeNotNull(@NonNull Update update) {
        if (update.message() == null) {
            throw new NullMessageException("Сообщение не должно быть null.");
        }
    }

    default boolean supports(@NonNull Update update) {
        messageMustBeNotNull(update);
        return update.message().text().equals(command());
    }

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
