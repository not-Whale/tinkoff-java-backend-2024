package edu.java.bot.link_processor.link_validators.impl;

import edu.java.bot.link_processor.link_validators.LinkValidator;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StackoverflowLinkValidator implements LinkValidator {
    private static final String STACKOVERFLOW_LINK_FORMAT = "(https?://)?stackoverflow.com/";

    private static final String QUESTION_LINK_FORMAT =
        STACKOVERFLOW_LINK_FORMAT + "question/(\\d{1,8})/([a-zA-Z\\-]{3,})/?";

    private static final String SEARCH_LINK_FORMAT =
        STACKOVERFLOW_LINK_FORMAT + "search?q=([\\w%]{3,})";

    public StackoverflowLinkValidator() {}

    @Override
    public boolean validate(@NonNull String link) {
        return link.matches(QUESTION_LINK_FORMAT) || link.matches(SEARCH_LINK_FORMAT);
    }
}
