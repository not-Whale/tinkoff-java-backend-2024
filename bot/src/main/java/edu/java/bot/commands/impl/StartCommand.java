package edu.java.bot.commands.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.repositories.user_repository.UserRepository;
import edu.java.bot.repositories.user_repository.user.State;
import edu.java.bot.response_creator.ResponseMessageCreator;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {
    private static final String COMMAND =
        "/start";

    private static final String DESCRIPTION =
        "Запустить бота и зарегистрироваться.";

    private final UserRepository userRepository;

    private final ResponseMessageCreator responseMessageCreator;

    public StartCommand(@NonNull UserRepository userRepository,
        @NonNull ResponseMessageCreator responseMessageCreator) {
        this.userRepository = userRepository;
        this.responseMessageCreator = responseMessageCreator;
    }

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public SendMessage process(@NonNull Update update) {
        processedUpdateFilter(update);
        User user = update.message().from();
        userRepository.createUserIfDoesNotExists(user.id());
        if (userRepository.getUserState(user.id()).equals(State.REGISTERED)) {
            return responseMessageCreator.getAlreadyRegisteredMessage(user);
        }
        userRepository.setUserState(user.id(), State.REGISTERED);
        return responseMessageCreator.getRegistrationMessage(user);
    }
}
