package edu.java.bot.response_creator.markup_processors;

import com.pengrad.telegrambot.model.request.ParseMode;
import java.util.List;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class MarkdownProcessor implements MarkupProcessor {
    private static final String BOLD = "*";

    private static final String ITALIC = "_";

    private static final String INLINE_CODE_BLOCK = "`";

    private static final String CODE_BLOCK = "```";

    private static final String INLINE_URL_FORMAT = "[%s](tg://user?id=%d)";

    private static final String INLINE_USER_MENTION_FORMAT = "[%s](%s)";

    private static final String ESCAPE = "\\";

    private static final String NEW_LINE = "\n";

    private static final String LEFT_BRACKET = "(";

    private static final List<String> ESCAPE_LIST = List.of(
        BOLD, ITALIC, INLINE_CODE_BLOCK, LEFT_BRACKET
    );

    public MarkdownProcessor() {}

    @Override
    public ParseMode parseMode() {
        return ParseMode.Markdown;
    }

    @Override
    public String bold(@NonNull String text) {
        return surround(escapeEntity(text, BOLD), BOLD);
    }

    @Override
    public String italic(@NonNull String text) {
        return surround(escapeEntity(text, ITALIC), ITALIC);
    }

    @Override
    public String inlineUrl(@NonNull String text, @NonNull String url) {
        return INLINE_URL_FORMAT.formatted(text, url);
    }

    @Override
    public String inlineUserMention(@NonNull String text, int userId) {
        return INLINE_USER_MENTION_FORMAT.formatted(text, userId);
    }

    @Override
    public String inlineCodeBlock(@NonNull String text) {
        return surround(text, INLINE_CODE_BLOCK);
    }

    @Override
    public String codeBlock(@NonNull String text) {
        return surround(surround(text, NEW_LINE), CODE_BLOCK);
    }

    @Override
    public String escape(@NonNull String text) {
        String escapedText = text;
        for (String symbol : ESCAPE_LIST) {
            escapedText = text.replace(symbol, ESCAPE + symbol);
        }
        return escapedText;
    }

    private String escapeEntity(String text, String entity) {
        StringBuilder escapedEntity = new StringBuilder();
        escapedEntity
            .append(entity)
            .append(ESCAPE)
            .repeat(entity, 2);
        return text.replace(entity, escapedEntity.toString());
    }

    private String surround(String text, String surround) {
        StringBuilder surroundedText = new StringBuilder();
        surroundedText
            .append(surround)
            .append(text)
            .append(surround);
        return surroundedText.toString();
    }
}
