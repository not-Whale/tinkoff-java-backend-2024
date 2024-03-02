package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exceptions.NullMessageException;
import edu.java.bot.repositories.user_repository.UserRepository;
import edu.java.bot.repositories.user_repository.user.State;
import lombok.NonNull;

public interface Command {
    String command();

    String description();

    SendMessage process(Update update);

    default String usage() {
        return command();
    }

    default boolean supports(@NonNull Update update) {
        messageMustBeNotNull(update);
        return update.message().text().equals(command());
    }

    default void messageMustBeNotNull(@NonNull Update update) {
        if (update.message() == null) {
            throw new NullMessageException("Сообщение не должно быть null.");
        }
    }

    default boolean isUserSessionNotStarted(@NonNull Update update, @NonNull UserRepository userRepository) {
        messageMustBeNotNull(update);
        User user = update.message().from();
        return (!userRepository.hasUser(user.id())
            || userRepository.getUserState(user.id()).equals(State.NOT_REGISTERED)
        );
    }

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
