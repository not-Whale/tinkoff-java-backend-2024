package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import edu.java.bot.exceptions.NullMessageException;
import edu.java.bot.repositories.user_repository.UserRepository;
import edu.java.bot.repositories.user_repository.user.State;
import java.util.List;
import lombok.NonNull;

public class CommandUtils {
    private CommandUtils() {}

    public static List<String> getArguments(@NonNull Update update) {
        messageMustBeNotNull(update);
        String[] messageSplit = update.message().text().split("\\s");
        return List.of(messageSplit).subList(1, messageSplit.length);
    }

    public static boolean isUserSessionNotStarted(@NonNull Update update, @NonNull UserRepository userRepository) {
        messageMustBeNotNull(update);
        User user = update.message().from();
        return (!userRepository.hasUser(user.id())
            || userRepository.getUserState(user.id()).equals(State.NOT_REGISTERED)
        );
    }

    public static void messageMustBeNotNull(@NonNull Update update) {
        if (update.message() == null) {
            throw new NullMessageException("Сообщение не должно быть null.");
        }
    }
}
