package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.exceptions.CommandDoesNotSupportsException;
import edu.java.bot.exceptions.NullMessageException;
import edu.java.bot.markdown_processor.MarkdownProcessor;
import lombok.NonNull;
import java.util.Arrays;

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

    default String[] arguments(@NonNull Update update) {
        if (!supports(update)) {
            throw new CommandDoesNotSupportsException(
                "Неверная команда. Ожидается: "
                    + command()
                    + ", получено: "
                    + update.message().text().split("\\s")[0]
            );
        }
        String[] commandSplit = update.message().text().split("\\s");
        return Arrays.copyOfRange(commandSplit, 1, commandSplit.length);
    }
}
