package edu.java.bot.link_parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;

public class StackoverflowParser implements LinkParser {
    private static final String STACKOVERFLOW_LINK_FORMAT = "(https?://)?stackoverflow.com/";

    private static final String QUESTION_LINK_FORMAT =
        STACKOVERFLOW_LINK_FORMAT + "question/(\\d{1,8})/([a-zA-Z\\-]{3,})/?";

    private static final String SEARCH_LINK_FORMAT =
        STACKOVERFLOW_LINK_FORMAT + "search?q=([\\w%]{3,})";

    private static final int QUESTION_ID_GROUP = 1;

    private static final int QUESTION_NAME_GROUP = 2;

    private static final int SEARCH_NAME_GROUP = 1;

    private final String link;

    @Getter
    private String questionId;

    @Getter
    private String questionName;

    @Getter
    private String searchName;

    private boolean isQuestion = false;

    private boolean isSearch = false;

    public StackoverflowParser(String link) {
        this.link = link;
    }

    @Override
    public boolean validate() {
        return link.matches(QUESTION_LINK_FORMAT) || link.matches(SEARCH_LINK_FORMAT);
    }

    @Override
    public void parse() {
        if (!validate()) {
            throw new RuntimeException();
        }
        Matcher questionLinkMatcher = Pattern.compile(QUESTION_LINK_FORMAT).matcher(link);
        if (questionLinkMatcher.matches()) {
            isQuestion = true;
            questionId = questionLinkMatcher.group(QUESTION_ID_GROUP);
            questionName = questionLinkMatcher.group(QUESTION_NAME_GROUP);
        }
        Matcher searchLinkMatcher = Pattern.compile(SEARCH_LINK_FORMAT).matcher(link);
        if (searchLinkMatcher.matches()) {
            isSearch = true;
            searchName = searchLinkMatcher.group(SEARCH_NAME_GROUP);
        }
    }

    public boolean isQuestion() {
        return isQuestion;
    }

    public boolean isSearch() {
        return isSearch;
    }
}
