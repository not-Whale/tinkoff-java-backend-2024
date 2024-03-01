package edu.java.bot.response_creator;

import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.response_creator.markup_processors.MarkupProcessor;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ResponseMessageCreator {
    private static final String NEW_LINE = "\n";

    private static final String DOUBLE_NEW_LINE = "\n\n";

    private static final String COMMAND_DELIMITER = " - ";

    private static final String LIST_ITEM = "- ";

    private static final String USE_HELP_COMMAND_MESSAGE =
        "Для получения инструкции по использованию бота используйте команду /help.";

    private static final String REGISTRATION_MESSAGE =
        "Вы успешно зарегистрированы." + NEW_LINE + USE_HELP_COMMAND_MESSAGE;

    private static final String ALREADY_REGISTERED_MESSAGE =
        "Вы уже зарегистрированы." + NEW_LINE + USE_HELP_COMMAND_MESSAGE;

    private static final String AVAILABLE_COMMANDS_MESSAGE =
        "Список доступных команд:";

    private static final String TRACK_LIST_EMPTY_MESSAGE =
        "Список отслеживаемых ресурсов пуст.";

    private static final String UNKNOWN_COMMAND_MESSAGE =
        "Неизвестная команда. " + USE_HELP_COMMAND_MESSAGE;

    private static final String MUST_USE_START_COMMAND_MESSAGE =
        "Для того, чтобы использовать бота, Вам необходимо зарегистрироваться с помощью команды /start.";

    private static final String TRACK_LIST_HEADER =
        "Список отслеживаемых ресурсов:";

    private final MarkupProcessor markupProcessor;

    public ResponseMessageCreator(@NonNull MarkupProcessor markupProcessor) {
        this.markupProcessor = markupProcessor;
    }

    public SendMessage getAlreadyRegisteredMessage(@NonNull User user) {
        return new SendMessage(
            user.id(),
            markupProcessor.escape(ALREADY_REGISTERED_MESSAGE)
        ).parseMode(markupProcessor.parseMode());
    }

    public SendMessage getRegistrationMessage(@NonNull User user) {
        return new SendMessage(
            user.id(),
            markupProcessor.escape(REGISTRATION_MESSAGE)
        ).parseMode(markupProcessor.parseMode());
    }

    public SendMessage getAvailableCommandsMessage(@NonNull User user, @NonNull List<Command> commands) {
        StringBuilder botCommandsMessageText = new StringBuilder(
            markupProcessor.bold(AVAILABLE_COMMANDS_MESSAGE)
        ).append(DOUBLE_NEW_LINE);
        for (Command command : commands) {
            botCommandsMessageText
                .append(markupProcessor.escape(command.command()))
                .append(markupProcessor.escape(COMMAND_DELIMITER))
                .append(markupProcessor.escape(command.description()))
                .append(NEW_LINE)
                .append(markupProcessor.codeBlock(command.usage()))
                .append(NEW_LINE);
        }
        return new SendMessage(
            user.id(),
            botCommandsMessageText.toString()
        ).parseMode(markupProcessor.parseMode());
    }

    public SendMessage getLinksGroupMessage(@NonNull User user, Map<String, @NonNull List<String>> groups) {
        StringBuilder messageText = new StringBuilder();
        for (var entry : groups.entrySet()) {
            messageText.append(getLinksListTextIfLinksPresent(entry.getKey(), entry.getValue()));
        }
        return new SendMessage(
            user.id(),
            messageText.toString()
        ).parseMode(markupProcessor.parseMode());
    }

    public SendMessage getTrackListMessage(@NonNull User user, @NonNull List<String> links) {
        return new SendMessage(
            user.id(),
            getLinksListTextIfLinksPresent(TRACK_LIST_HEADER, links)
        ).parseMode(markupProcessor.parseMode());
    }

    public SendMessage getTrackListEmptyMessage(@NonNull User user) {
        return new SendMessage(
            user.id(),
            markupProcessor.escape(TRACK_LIST_EMPTY_MESSAGE)
        ).parseMode(markupProcessor.parseMode());
    }

    public SendMessage getNoLinksMessage(@NonNull User user, @NonNull String message) {
        return new SendMessage(
            user.id(),
            markupProcessor.escape(message) + NEW_LINE + markupProcessor.escape(USE_HELP_COMMAND_MESSAGE)
        ).parseMode(markupProcessor.parseMode());
    }

    public SendMessage getMustStartMessage(@NonNull User user) {
        return new SendMessage(
            user.id(),
            markupProcessor.escape(MUST_USE_START_COMMAND_MESSAGE)
        ).parseMode(markupProcessor.parseMode());
    }

    public SendMessage getUnknownCommandMessage(@NonNull User user) {
        return new SendMessage(
            user.id(),
            markupProcessor.escape(UNKNOWN_COMMAND_MESSAGE)
        ).parseMode(markupProcessor.parseMode());
    }

    private String getLinksListTextIfLinksPresent(String headerText, List<String> links) {
        StringBuilder linksList = new StringBuilder();
        if (!links.isEmpty()) {
            linksList
                .append(markupProcessor.bold(headerText))
                .append(NEW_LINE);
            for (String link : links) {
                linksList
                    .append(markupProcessor.escape(LIST_ITEM))
                    .append(markupProcessor.escape(link))
                    .append(NEW_LINE);
            }
            linksList.append(NEW_LINE);
        }
        return linksList.toString();
    }
}
