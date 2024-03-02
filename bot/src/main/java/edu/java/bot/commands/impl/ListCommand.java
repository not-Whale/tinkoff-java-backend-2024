package edu.java.bot.commands.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.repositories.user_repository.UserRepository;
import edu.java.bot.response_creator.ResponseMessageCreator;
import java.util.List;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements Command {
    private static final String COMMAND =
        "/list";

    private static final String DESCRIPTION =
        "Вывести список отслеживаемых ресурсов.";

    private final UserRepository userRepository;

    private final ResponseMessageCreator responseMessageCreator;

    public ListCommand(@NonNull UserRepository userRepository,
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
        messageMustBeNotNull(update);
        User user = update.message().from();
        if (isUserSessionNotStarted(update, userRepository)) {
            return responseMessageCreator.getMustStartMessage(user);
        }
        List<String> userLinks = userRepository.getUserLinks(user.id());
        if (userLinks.isEmpty()) {
            return responseMessageCreator.getTrackListEmptyMessage(user);
        }
        return responseMessageCreator.getTrackListMessage(user, userLinks);
    }
}
