package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.markdown_processor.MarkdownProcessor;

public interface Command {
    String command();

    String description();

    CommandType type();

    default String usage() {
        return MarkdownProcessor.codeBlock(command());
    }

    default boolean supports(Update update) {
        return update.message().text().equals(command());
    }

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
