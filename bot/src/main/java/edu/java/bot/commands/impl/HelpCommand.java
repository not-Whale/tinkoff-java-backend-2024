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
public class HelpCommand implements Command {
    private static final String COMMAND =
        "/help";

    private static final String DESCRIPTION =
        "Показать список команд.";

    private final ResponseMessageCreator responseMessageCreator;

    private final List<Command> commands;

    public HelpCommand(@NonNull UserRepository userRepository,
        @NonNull ResponseMessageCreator responseMessageCreator,
        @NonNull List<Command> commands) {
        this.responseMessageCreator = responseMessageCreator;
        this.commands = commands;
        this.commands.add(this);
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
        return responseMessageCreator.getAvailableCommandsMessage(user, commands);
    }
}
