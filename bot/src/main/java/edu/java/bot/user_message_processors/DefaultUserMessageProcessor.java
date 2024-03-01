package edu.java.bot.user_message_processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.exceptions.NullMessageException;
import edu.java.bot.link_parsers.GithubParser;
import edu.java.bot.link_parsers.StackoverflowParser;
import edu.java.bot.repositories.user_repository.user.State;
import edu.java.bot.response_processors.markup_processors.MarkdownProcessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;

public class DefaultUserMessageProcessor implements UserMessageProcessor {
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

    private final List<Command> commands;

    public DefaultUserMessageProcessor(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public List<Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(@NonNull Update update) {
        if (update.message() == null) {
            throw new NullMessageException("Сообщение не должно быть null.");
        }
        Optional<SendMessage> response = commands.stream()
            .filter(command -> command.supports(update))
            .map(command -> command.process(update))
            .findFirst();
        return response.orElse(unknownCommand(update));
    }

    private SendMessage processStartCommand(@NonNull Update update) {
        User user = update.message().from();
        userRepository.createUserIfDoesNotExists(user.id());
        if (userRepository.getUserState(user.id()).equals(State.REGISTERED)) {
            return new SendMessage(user.id(), ALREADY_REGISTERED_MESSAGE);
        }
        userRepository.setUserState(user.id(), State.REGISTERED);
        return new SendMessage(user.id(), REGISTRATION_MESSAGE);
    }

    private SendMessage processHelpCommand(@NonNull Update update) {
        User user = update.message().from();
        StringBuilder botCommandsMessageText = new StringBuilder(
            MarkdownProcessor.bold(AVAILABLE_COMMANDS_MESSAGE)
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
        ).parseMode(ParseMode.Markdown);
    }

    private SendMessage processTrackCommand(@NonNull Update update, @NonNull String[] arguments) {
        User user = update.message().from();
        if (userSessionIsNotStarted(update)) {
            return mustStartMessage(update);
        }
        if (arguments.length == 0) {
            return new SendMessage(
                user.id(),
                NO_TRACK_LINKS_MESSAGE
            );
        }
        List<String> validatedLinks = getValidatedLinks(arguments);
        List<String> trackedLinks = new ArrayList<>();
        List<String> alreadyTrackedLinks = new ArrayList<>();
        for (String link : validatedLinks) {
            if (userRepository.trackLinkForUser(user.id(), link)) {
                trackedLinks.add(link);
            } else {
                alreadyTrackedLinks.add(link);
            }
        }
        List<String> unvalidatedLinks = getUnvalidatedLinks(arguments);
        String response = getLinksGroupMessageText(new HashMap<>() {{
            put(MarkdownProcessor.bold(TRACKED_LINKS_HEADER), trackedLinks);
            put(MarkdownProcessor.bold(ALREADY_TRACKED_LINKS_HEADER), alreadyTrackedLinks);
            put(MarkdownProcessor.bold(UNVALIDATED_LINKS_HEADER), unvalidatedLinks);
        }});
        return new SendMessage(
            user.id(),
            response
        ).parseMode(ParseMode.Markdown);
    }

    private SendMessage processUntrackCommand(@NonNull Update update, @NonNull String[] arguments) {
        User user = update.message().from();
        if (userSessionIsNotStarted(update)) {
            return mustStartMessage(update);
        }
        if (arguments.length == 0) {
            return new SendMessage(
                user.id(),
                NO_UNTRACK_LINKS_MESSAGE
            );
        }
        List<String> validatedLinks = getValidatedLinks(arguments);
        List<String> untrackedLinks = new ArrayList<>();
        List<String> alreadyUntrackedLinks = new ArrayList<>();
        for (String link : validatedLinks) {
            if (userRepository.untrackLinkForUser(user.id(), link)) {
                untrackedLinks.add(link);
            } else {
                alreadyUntrackedLinks.add(link);
            }
        }
        List<String> unvalidatedLinks = getUnvalidatedLinks(arguments);
        String response = getLinksGroupMessageText(new HashMap<>() {{
            put(MarkdownProcessor.bold(UNTRACKED_LINKS_HEADER), untrackedLinks);
            put(MarkdownProcessor.bold(ALREADY_UNTRACKED_LINKS_HEADER), alreadyUntrackedLinks);
            put(MarkdownProcessor.bold(UNVALIDATED_LINKS_HEADER), unvalidatedLinks);
        }});
        return new SendMessage(
            user.id(),
            response
        ).parseMode(ParseMode.Markdown);
    }

    private List<String> getUnvalidatedLinks(@NonNull String[] links) {
        List<String> unvalidatedLinks = new ArrayList<>();
        for (String link : links) {
            GithubParser githubParser = new GithubParser(link);
            StackoverflowParser stackoverflowParser = new StackoverflowParser(link);
            if (!githubParser.validate() && !stackoverflowParser.validate()) {
                unvalidatedLinks.add(link);
            }
        }
        return unvalidatedLinks;
    }

    private List<String> getValidatedLinks(@NonNull String[] links) {
        List<String> validatedLinks = new ArrayList<>();
        for (String link : links) {
            GithubParser githubParser = new GithubParser(link);
            StackoverflowParser stackoverflowParser = new StackoverflowParser(link);
            if (githubParser.validate() || stackoverflowParser.validate()) {
                validatedLinks.add(link);
            }
        }
        return validatedLinks;
    }

    private String getLinksGroupMessageText(@NonNull Map<String, List<String>> groups) {
        StringBuilder messageText = new StringBuilder();
        for (var entry : groups.entrySet()) {
            messageText.append(getLinksListMessageTextIfPresent(entry.getKey(), entry.getValue()));
        }
        return messageText.toString();
    }

    private String getLinksListMessageTextIfPresent(@NonNull String headerText, @NonNull List<String> links) {
        StringBuilder linksList = new StringBuilder();
        if (!links.isEmpty()) {
            linksList
                .append(headerText)
                .append(NEW_LINE);
            for (String link : links) {
                linksList
                    .append("- ")
                    .append(link)
                    .append(NEW_LINE);
            }
            linksList.append(NEW_LINE);
        }
        return linksList.toString();
    }

    private SendMessage processListCommand(@NonNull Update update) {
        User user = update.message().from();
        if (userSessionIsNotStarted(update)) {
            return mustStartMessage(update);
        }
        String[] userLinks = userRepository.getUserLinks(user.id());
        if (userLinks.length == 0) {
            return new SendMessage(
                user.id(),
                TRACK_LIST_EMPTY_MESSAGE
            );
        }
        String response = getLinksGroupMessageText(new HashMap<>() {{
            put(MarkdownProcessor.bold(TRACK_LIST_HEADER), List.of(userLinks));
        }});
        return new SendMessage(
            user.id(),
            response
        ).parseMode(ParseMode.Markdown);
    }

    private SendMessage unknownCommand(@NonNull Update update) {
        User user = update.message().from();
        return new SendMessage(
            user.id(),
            UNKNOWN_COMMAND_MESSAGE
        );
    }

    private SendMessage mustStartMessage(@NonNull Update update) {
        User user = update.message().from();
        return new SendMessage(
            user.id(),
            MUST_USE_START_COMMAND_MESSAGE
        );
    }

    private boolean userSessionIsNotStarted(@NonNull Update update) {
        User user = update.message().from();
        return (!userRepository.hasUser(user.id()) || userRepository.getUserState(user.id()).equals(State.NOT_REGISTERED));
    }
}
