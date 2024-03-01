package edu.java.bot.link_processor.link_parsers.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.java.bot.exceptions.IncorrectLinkFormatException;
import edu.java.bot.link_processor.link_parsers.LinkParser;
import lombok.Getter;
import lombok.NonNull;

public class GithubParser implements LinkParser {
    private static final String GITHUB_LINK_FORMAT = "(https?://)?github.com/([a-zA-Z\\-]{3,})/([a-zA-Z0-9\\-]{3,})/?";

    private static final int USER_GROUP = 2;

    private static final int REPOSITORY_GROUP = 3;

    private final String link;

    @Getter
    private String user;

    @Getter
    private String repository;

    public GithubParser(@NonNull String link) {
        this.link = link;
    }

    @Override
    public boolean validate() {
        return link.matches(GITHUB_LINK_FORMAT);
    }

    @Override
    public void parse() {
        Matcher linkMatcher = Pattern.compile(GITHUB_LINK_FORMAT).matcher(link);
        if (!linkMatcher.matches()) {
            throw new IncorrectLinkFormatException("Ссылка не соответствует шаблону \""
                + GITHUB_LINK_FORMAT
                + "\"."
            );
        }
        this.user = linkMatcher.group(USER_GROUP);
        this.repository = linkMatcher.group(REPOSITORY_GROUP);
    }
}
