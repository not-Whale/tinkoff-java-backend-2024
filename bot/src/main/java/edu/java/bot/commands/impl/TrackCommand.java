package edu.java.bot.commands.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
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
public class TrackCommand implements CommandWithArguments {
    private static final String COMMAND =
        "/track";

    private static final String DESCRIPTION =
        "Добавить ресурс в список отслеживаемых.";

    private static final String TRACKED_LINKS_HEADER =
        "Ресурсы, которые были добавлены в список отслеживаемых:";

    private static final String ALREADY_TRACKED_LINKS_HEADER =
        "Ресурсы, которые уже находятся в списке отслеживаемых:";

    private static final String UNVALIDATED_LINKS_HEADER =
        "Ресурсы, ссылки на которые не были распознаны:";

    private static final String NO_TRACK_LINKS_MESSAGE =
        "Не было передано ссылок для отслеживания.";

    private final UserRepository userRepository;

    private final ResponseMessageCreator responseMessageCreator;

    private final LinkProcessor linkProcessor;

    public TrackCommand(@NonNull UserRepository userRepository,
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
        if (isUserSessionNotStarted(update, userRepository)) {
            return responseMessageCreator.getMustStartMessage(user);
        }
        List<String> links = getArguments(update);
        if (links.isEmpty()) {
            return responseMessageCreator.getNoLinksMessage(user, NO_TRACK_LINKS_MESSAGE);
        }
        List<String> validatedLinks = linkProcessor.getValidatedLinks(links);
        List<String> trackedLinks = new ArrayList<>();
        List<String> alreadyTrackedLinks = new ArrayList<>();
        for (String link : validatedLinks) {
            if (userRepository.trackLinkForUser(user.id(), link)) {
                trackedLinks.add(link);
            } else {
                alreadyTrackedLinks.add(link);
            }
        }
        return responseMessageCreator.getLinksGroupMessage(user,
            Map.of(
                TRACKED_LINKS_HEADER, trackedLinks,
                ALREADY_TRACKED_LINKS_HEADER, alreadyTrackedLinks,
                UNVALIDATED_LINKS_HEADER, linkProcessor.getUnvalidatedLinks(links)
            )
        );
    }
}
