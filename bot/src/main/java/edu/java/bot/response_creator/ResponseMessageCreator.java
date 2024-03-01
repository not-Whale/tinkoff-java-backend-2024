package edu.java.bot.response_creators;

import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.response_creators.markup_processors.MarkupProcessor;
import java.util.List;

public class ResponseMessageCreator {
    private static final String NEW_LINE = "\n";

    private static final String DOUBLE_NEW_LINE = "\n\n";

    private static final String USE_HELP_COMMAND_MESSAGE =
        "Для получения инструкции по использованию бота используйте команду /help.";

    private static final String REGISTRATION_MESSAGE =
        "Вы успешно зарегистрированы. " + USE_HELP_COMMAND_MESSAGE;

    private static final String ALREADY_REGISTERED_MESSAGE =
        "Вы уже зарегистрированы. " + USE_HELP_COMMAND_MESSAGE;

    private static final String AVAILABLE_COMMANDS_MESSAGE =
        "Список доступных команд:";

    private static final String NO_TRACK_LINKS_MESSAGE =
        "Не было передано ссылок для отслеживания. " + USE_HELP_COMMAND_MESSAGE;

    private static final String NO_UNTRACK_LINKS_MESSAGE =
        "Не было передано ссылок для удаления. " + USE_HELP_COMMAND_MESSAGE;

    private static final String TRACK_LIST_EMPTY_MESSAGE =
        "Список отслеживаемых ресурсов пуст.";

    private static final String UNKNOWN_COMMAND_MESSAGE =
        "Неизвестная команда. " + USE_HELP_COMMAND_MESSAGE;

    private static final String MUST_USE_START_COMMAND_MESSAGE =
        "Для того, чтобы использовать бота, Вам необходимо зарегистрироваться с помощью команды /start.";

    private static final String TRACK_LIST_HEADER =
        "Список отслеживаемых ресурсов:";

    private static final String TRACKED_LINKS_HEADER =
        "Ресурсы, которые были добавлены в список отслеживаемых:";

    private static final String ALREADY_TRACKED_LINKS_HEADER =
        "Ресурсы, которые уже находятся в списке отслеживаемых:";

    private static final String UNTRACKED_LINKS_HEADER =
        "Ресурсы, которые были удалены из списка отслеживаемых:";

    private static final String ALREADY_UNTRACKED_LINKS_HEADER =
        "Ресурсы, которых нет в списке отслеживаемых:";

    private static final String UNVALIDATED_LINKS_HEADER =
        "Ресурсы, ссылки на которые не были распознаны:";

    private final MarkupProcessor markupProcessor;

    public ResponseMessageCreator(MarkupProcessor markupProcessor) {
        this.markupProcessor = markupProcessor;
    }

    public SendMessage getAlreadyRegisteredMessage(User user) {
        return new SendMessage(user.id(), ALREADY_REGISTERED_MESSAGE).parseMode(markupProcessor.parseMode());
    }

    public SendMessage getRegistrationMessage(User user) {
        return new SendMessage(user.id(), REGISTRATION_MESSAGE).parseMode(markupProcessor.parseMode());
    }

    public SendMessage getAvailableCommandsMessage(User user, List<Command> commands) {
        StringBuilder botCommandsMessageText = new StringBuilder(
            markupProcessor.bold(AVAILABLE_COMMANDS_MESSAGE)
        ).append(DOUBLE_NEW_LINE);
        for (Command command : commands) {
            botCommandsMessageText
                .append(command.command())
                .append(" - ")
                .append(command.description())
                .append(NEW_LINE)
                .append(command.usage())
                .append(NEW_LINE);
        }
        return new SendMessage(
            user.id(),
            botCommandsMessageText.toString()
        ).parseMode(markupProcessor.parseMode());
    }



    public SendMessage getNoArgumentsMessage(User user) {
        return new SendMessage(
            user.id(),
            NO_TRACK_LINKS_MESSAGE
        ).parseMode(markupProcessor.parseMode());
    }

    public SendMessage getMustStartMessage(User user) {
        return new SendMessage(
            user.id(),
            MUST_USE_START_COMMAND_MESSAGE
        ).parseMode(markupProcessor.parseMode());
    }
}
