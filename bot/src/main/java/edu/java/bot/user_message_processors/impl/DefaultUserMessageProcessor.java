package edu.java.bot.user_message_processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.exceptions.NullMessageException;
import edu.java.bot.link_parsers.impl.GithubParser;
import edu.java.bot.link_parsers.impl.StackoverflowParser;
import edu.java.bot.response_creators.markup_processors.MarkdownProcessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;

public class DefaultUserMessageProcessor implements UserMessageProcessor {
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

    private SendMessage processTrackCommand(@NonNull Update update, @NonNull String[] arguments) {

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
}
