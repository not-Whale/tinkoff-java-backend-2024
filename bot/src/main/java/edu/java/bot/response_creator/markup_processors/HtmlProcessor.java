package edu.java.bot.response_creator.markup_processors;

import com.pengrad.telegrambot.model.request.ParseMode;
import java.util.Map;

public class HtmlProcessor implements MarkupProcessor {
    private static final String BOLD_FORMAT = "<b>%s</b>";

    private static final String ITALIC_FORMAT = "<i>%s</i>";

    private static final String INLINE_URL_FORMAT = "<a href=\"%s\">%s</a>";

    private static final String INLINE_USER_MENTION_FORMAT = "<a href=\"tg://user?id=%s\">%d</a>";

    private static final String INLINE_CODE_BLOCK_FORMAT = "<code>%s</code>";

    private static final String CODE_BLOCK_FORMAT = "<pre>%s</pre>";

    private static final Map<String, String> ESCAPE_MAP = Map.of(
        "<", "&lt;",
        ">", "&gt;",
        "&", "&amp;"
    );

    @Override
    public ParseMode parseMode() {
        return ParseMode.HTML;
    }

    @Override
    public String bold(String text) {
        return BOLD_FORMAT.formatted(text);
    }

    @Override
    public String italic(String text) {
        return ITALIC_FORMAT.formatted(text);
    }

    @Override
    public String inlineUrl(String text, String url) {
        return INLINE_URL_FORMAT.formatted(text, url);
    }

    @Override
    public String inlineUserMention(String text, int userId) {
        return INLINE_USER_MENTION_FORMAT.formatted(text, userId);
    }

    @Override
    public String inlineCodeBlock(String text) {
        return INLINE_CODE_BLOCK_FORMAT.formatted(text);
    }

    @Override
    public String codeBlock(String text) {
        return CODE_BLOCK_FORMAT.formatted(text);
    }

    @Override
    public String escape(String text) {
        String escapedText = text;
        for (var entry : ESCAPE_MAP.entrySet()) {
            escapedText = escapedText.replace(entry.getKey(), entry.getValue());
        }
        return escapedText;
    }
}
