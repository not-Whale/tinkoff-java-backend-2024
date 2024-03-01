package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.exceptions.NullMessageException;
import edu.java.bot.response_processors.markup_processors.MarkdownProcessor;
import lombok.NonNull;

public interface CommandWithArguments extends Command {
    @Override
    default String usage() {
        return MarkdownProcessor.codeBlock(command() + " <link1 link2...>");
    }

    @Override
    default boolean supports(@NonNull Update update) {
        if (update.message() == null) {
            throw new NullMessageException("Сообщение не должно быть null.");
        }
        return update.message().text().startsWith(command());
    }
}
