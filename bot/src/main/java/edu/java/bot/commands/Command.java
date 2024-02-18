package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.db.Database;

public interface Command {
    String command();

    String description();

    SendMessage handle(Update update);

    void setDatabase(Database database);

    default boolean validate(Update update) {
        return update.message().text().startsWith(command());
    }

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
