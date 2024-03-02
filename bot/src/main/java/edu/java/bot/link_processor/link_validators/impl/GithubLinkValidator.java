package edu.java.bot.link_processor.link_validators.impl;

import edu.java.bot.link_processor.link_validators.LinkValidator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class GithubLinkValidator implements LinkValidator {
    private static final String GITHUB_LINK_FORMAT = "(https?://)?github.com/([a-zA-Z\\-]{3,})/([a-zA-Z0-9\\-]{3,})/?";

    public GithubLinkValidator() {}

    @Override
    public List<String> patterns() {
        return List.of(GITHUB_LINK_FORMAT);
    }
}
