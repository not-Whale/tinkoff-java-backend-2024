package edu.java.bot.link_parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;

public class GithubParser implements LinkParser {
    private static final String GITHUB_LINK_FORMAT = "(https?://)?github.com/([a-zA-Z\\-]{3,})/([a-zA-Z0-9\\-]{3,})/?";

    private static final int USER_GROUP = 2;

    private static final int REPOSITORY_GROUP = 3;

    private final String link;

    @Getter
    private String user;

    @Getter
    private String repository;

    public GithubParser(String link) {
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
            throw new RuntimeException();
        }
        this.user = linkMatcher.group(USER_GROUP);
        this.repository = linkMatcher.group(REPOSITORY_GROUP);
    }
}
