package edu.java.bot.user_message_processors.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandUtils;
import edu.java.bot.response_creator.ResponseMessageCreator;
import edu.java.bot.user_message_processors.UserMessageProcessor;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserMessageProcessor implements UserMessageProcessor {
    private final ResponseMessageCreator responseMessageCreator;

    private final List<Command> commands;

    public DefaultUserMessageProcessor(@NonNull ResponseMessageCreator responseMessageCreator,
        @NonNull List<Command> commands) {
        this.responseMessageCreator = responseMessageCreator;
        this.commands = commands;
    }

    @Override
    public List<Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(@NonNull Update update) {
        CommandUtils.messageMustBeNotNull(update);
        Optional<SendMessage> response = commands.stream()
            .filter(command -> command.supports(update))
            .map(command -> command.process(update))
            .findFirst();
        return response.orElse(unknownCommand(update));
    }

    private SendMessage unknownCommand(Update update) {
        User user = update.message().from();
        return responseMessageCreator.getUnknownCommandMessage(user);
    }
}
