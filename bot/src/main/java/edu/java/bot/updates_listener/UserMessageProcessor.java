package edu.java.bot.updates_listener;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.db.UserRepository;
import edu.java.bot.db.user.State;
import edu.java.bot.markdown_processor.MarkdownProcessor;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

public class UserMessageProcessor {
    private static final String NEW_LINE = "\n";

    private static final String DOUBLE_NEW_LINE = "\n\n";

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
            .filter(commands1 -> commands1.isUpdateContainsCommand(update))
            .findFirst();
        if (userCommand.isEmpty()) {
            return unknownCommand(update);
        }
        return switch (userCommand.get().type()) {
            case START -> processStartCommand(update);
            case HELP -> processHelpCommand(update);
            case TRACK -> processTrackCommand(update);
            case UNTRACK -> processUntrackCommand(update);
            case LIST -> processListCommand(update);
        };
    }

    private SendMessage processStartCommand(Update update) {
        User user = update.message().from();
        userRepository.createUser(user.id());
        if (userRepository.getUserState(user.id()).equals(State.DEFAULT)) {
            return new SendMessage(user.id(), "Вы уже зарегистрированы.");
        }
        userRepository.setUserState(user.id(), State.DEFAULT);
        return new SendMessage(user.id(), "Вы успешно зарегистрированы.");
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

    private SendMessage processTrackCommand(Update update) {
        User user = update.message().from();
        if (userSessionIsNotStarted(update)) {
            return mustStartMessage(update);
        }
        // TODO: сделать процессор аргументов
        // TODO: сделать парсер ссылок
        return new SendMessage(
            user.id(),
            "Команда /track отработала."
        );
    }

    private SendMessage processUntrackCommand(Update update) {
        User user = update.message().from();
        if (userSessionIsNotStarted(update)) {
            return mustStartMessage(update);
        }
        // TODO: сделать процессор аргументов
        return new SendMessage(
            user.id(),
            "Команда /untrack отработала."
        );
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
        StringBuilder userLinksMessageText = new StringBuilder(
            MarkdownProcessor.bold("Список отслеживаемых ресурсов:")
        ).append(NEW_LINE);
        for (String link : userLinks) {
            userLinksMessageText
                .append("- ")
                .append(link)
                .append(NEW_LINE);
        }
        return new SendMessage(
            user.id(),
            userLinksMessageText.toString()
        ).parseMode(ParseMode.Markdown);
    }

    private SendMessage unknownCommand(Update update) {
        User user = update.message().from();
        return new SendMessage(
            user.id(),
            "Неизвестная инструкция. Для получения инструкции по использованию бота используйте команду /help."
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
