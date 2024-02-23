package edu.java.bot.updates_listener;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandWithArguments;
import edu.java.bot.db.UserRepository;
import edu.java.bot.db.user.State;
import edu.java.bot.link_parser.GithubParser;
import edu.java.bot.link_parser.StackoverflowParser;
import edu.java.bot.markdown_processor.MarkdownProcessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;

public class UserMessageProcessor {
    private static final String NEW_LINE = "\n";

    private static final String DOUBLE_NEW_LINE = "\n\n";

    private static final String USE_HELP_COMMAND_TEXT =
        "Для получения инструкции по использованию бота используйте команду /help.";

    private static final String UNVALIDATED_LINKS_TEXT =
        "Ресурсы, ссылки на которые не были распознаны:";

    private final UserRepository userRepository;

    @Getter
    private final List<Command> commands;

    public UserMessageProcessor(UserRepository userRepository, List<Command> commands) {
        this.userRepository = userRepository;
        this.commands = commands;
    }

    public SendMessage process(Update update) {
        if (update.message() == null) {
            return null;
        }
        Optional<Command> userCommand = commands.stream()
            .filter(commands1 -> commands1.supports(update))
            .findFirst();
        if (userCommand.isEmpty()) {
            return unknownCommand(update);
        }
        return switch (userCommand.get().type()) {
            case START -> processStartCommand(update);
            case HELP -> processHelpCommand(update);
            case TRACK -> processTrackCommand(update, ((CommandWithArguments) userCommand.get()).arguments(update));
            case UNTRACK -> processUntrackCommand(update, ((CommandWithArguments) userCommand.get()).arguments(update));
            case LIST -> processListCommand(update);
        };
    }

    private SendMessage processStartCommand(Update update) {
        User user = update.message().from();
        userRepository.createUser(user.id());
        if (userRepository.getUserState(user.id()).equals(State.DEFAULT)) {
            return new SendMessage(user.id(), "Вы уже зарегистрированы. " + USE_HELP_COMMAND_TEXT);
        }
        userRepository.setUserState(user.id(), State.DEFAULT);
        return new SendMessage(user.id(), "Вы успешно зарегистрированы. " + USE_HELP_COMMAND_TEXT);
    }

    private SendMessage processHelpCommand(Update update) {
        User user = update.message().from();
        StringBuilder botCommandsMessageText = new StringBuilder(
            MarkdownProcessor.bold("Список доступных команд:")
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

    private SendMessage processTrackCommand(Update update, String[] arguments) {
        User user = update.message().from();
        if (userSessionIsNotStarted(update)) {
            return mustStartMessage(update);
        }
        if (arguments.length == 0) {
            return new SendMessage(
                user.id(),
                "Не было передано ссылок для отслеживания. " + USE_HELP_COMMAND_TEXT
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
            put(MarkdownProcessor.bold("Ресурсы, которые были добавлены в список отслеживаемых:"), trackedLinks);
            put(MarkdownProcessor.bold("Ресурсы, которые уже находятся в списке отслеживаемых:"), alreadyTrackedLinks);
            put(MarkdownProcessor.bold(UNVALIDATED_LINKS_TEXT), unvalidatedLinks);
        }});
        return new SendMessage(
            user.id(),
            response
        ).parseMode(ParseMode.Markdown);
    }

    private SendMessage processUntrackCommand(Update update, String[] arguments) {
        User user = update.message().from();
        if (userSessionIsNotStarted(update)) {
            return mustStartMessage(update);
        }
        if (arguments.length == 0) {
            return new SendMessage(
                user.id(),
                "Не было передано ссылок для удаления. " + USE_HELP_COMMAND_TEXT
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
            put(MarkdownProcessor.bold("Ресурсы, которые были удалены из списка отслеживаемых:"), untrackedLinks);
            put(MarkdownProcessor.bold("Ресурсы, которых нет в списке отслеживаемых:"), alreadyUntrackedLinks);
            put(MarkdownProcessor.bold(UNVALIDATED_LINKS_TEXT), unvalidatedLinks);
        }});
        return new SendMessage(
            user.id(),
            response
        ).parseMode(ParseMode.Markdown);
    }

    private List<String> getUnvalidatedLinks(String[] links) {
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

    private List<String> getValidatedLinks(String[] links) {
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

    private String getLinksGroupMessageText(Map<String, List<String>> groups) {
        StringBuilder messageText = new StringBuilder();
        for (var entry : groups.entrySet()) {
            messageText.append(getLinksListMessageTextIfPresent(entry.getKey(), entry.getValue()));
        }
        return messageText.toString();
    }

    private String getLinksListMessageTextIfPresent(String headerText, List<String> links) {
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

    private SendMessage processListCommand(Update update) {
        User user = update.message().from();
        if (userSessionIsNotStarted(update)) {
            return mustStartMessage(update);
        }
        String[] userLinks = userRepository.getUserLinks(user.id());
        if (userLinks.length == 0) {
            return new SendMessage(
                user.id(),
                "Список отслеживаемых ресурсов пуст."
            );
        }
        String response = getLinksGroupMessageText(new HashMap<>() {{
            put(MarkdownProcessor.bold("Список отслеживаемых ресурсов:"), List.of(userLinks));
        }});
        return new SendMessage(
            user.id(),
            response
        ).parseMode(ParseMode.Markdown);
    }

    private SendMessage unknownCommand(Update update) {
        User user = update.message().from();
        return new SendMessage(
            user.id(),
            "Неизвестная инструкция. " + USE_HELP_COMMAND_TEXT
        );
    }

    private SendMessage mustStartMessage(Update update) {
        User user = update.message().from();
        return new SendMessage(
            user.id(),
            "Для того, чтобы использовать бота, Вам необходимо зарегистрироваться с помощью команды /start."
        );
    }

    private boolean userSessionIsNotStarted(Update update) {
        User user = update.message().from();
        return (!userRepository.hasUser(user.id()) || userRepository.getUserState(user.id()).equals(State.NEW_USER));
    }
}
