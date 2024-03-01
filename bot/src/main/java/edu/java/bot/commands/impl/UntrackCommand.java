package edu.java.bot.commands.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.CommandUtils;
import edu.java.bot.commands.CommandWithArguments;
import edu.java.bot.link_processor.LinkProcessor;
import edu.java.bot.repositories.user_repository.UserRepository;
import edu.java.bot.response_creator.ResponseMessageCreator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class UntrackCommand implements CommandWithArguments {
    private static final String COMMAND =
        "/untrack";

    private static final String DESCRIPTION =
        "Удалить ресурс из списка отслеживаемых.";

    private static final String UNTRACKED_LINKS_HEADER =
        "Ресурсы, которые были удалены из списка отслеживаемых:";

    private static final String ALREADY_UNTRACKED_LINKS_HEADER =
        "Ресурсы, которых нет в списке отслеживаемых:";

    private static final String UNVALIDATED_LINKS_HEADER =
        "Ресурсы, ссылки на которые не были распознаны:";

    private static final String NO_UNTRACK_LINKS_MESSAGE =
        "Не было передано ссылок для удаления.";

    private final UserRepository userRepository;

    private final ResponseMessageCreator responseMessageCreator;

    private final LinkProcessor linkProcessor;

    public UntrackCommand(@NonNull UserRepository userRepository,
        @NonNull ResponseMessageCreator responseMessageCreator,
        @NonNull LinkProcessor linkProcessor) {
        this.userRepository = userRepository;
        this.responseMessageCreator = responseMessageCreator;
        this.linkProcessor = linkProcessor;
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
        if (CommandUtils.isUserSessionNotStarted(update, userRepository)) {
            return responseMessageCreator.getMustStartMessage(user);
        }
        List<String> links = CommandUtils.getArguments(update);
        if (links.isEmpty()) {
            return responseMessageCreator.getNoLinksMessage(user, NO_UNTRACK_LINKS_MESSAGE);
        }
        List<String> validatedLinks = linkProcessor.getValidatedLinks(links);
        List<String> untrackedLinks = new ArrayList<>();
        List<String> alreadyUntrackedLinks = new ArrayList<>();
        for (String link : validatedLinks) {
            if (userRepository.untrackLinkForUser(user.id(), link)) {
                untrackedLinks.add(link);
            } else {
                alreadyUntrackedLinks.add(link);
            }
        }
        return responseMessageCreator.getLinksGroupMessage(user,
            Map.of(
                UNTRACKED_LINKS_HEADER, untrackedLinks,
                ALREADY_UNTRACKED_LINKS_HEADER, alreadyUntrackedLinks,
                UNVALIDATED_LINKS_HEADER, linkProcessor.getUnvalidatedLinks(links)
            )
        );
    }
}
