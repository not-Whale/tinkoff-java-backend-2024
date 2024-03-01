package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exceptions.NullMessageException;
import edu.java.bot.response_processors.markup_processors.MarkdownProcessor;
import lombok.NonNull;

public interface Command {
    String command();

    String description();

    SendMessage process(@NonNull Update update);

    boolean isRegistrationRequired();

    default String usage() {
        return MarkdownProcessor.codeBlock(command());
    }

    default boolean supports(@NonNull Update update) {
        if (update.message() == null) {
            throw new NullMessageException("Сообщение не должно быть null.");
        }
        return update.message().text().equals(command());
    }

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
