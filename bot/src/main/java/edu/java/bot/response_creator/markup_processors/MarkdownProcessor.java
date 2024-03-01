package edu.java.bot.response_creators.markup_processors;

import com.pengrad.telegrambot.model.request.ParseMode;
import lombok.NonNull;

public class MarkdownProcessor implements MarkupProcessor {
    private MarkdownProcessor() {}

    @Override
    public ParseMode parseMode() {
        return ParseMode.Markdown;
    }

    public String bold(@NonNull String text) {
        return surround(text, "*");
    }

    public String italic(@NonNull String text) {
        return surround(text, "_");
    }

    public String underline(@NonNull String text) {
        return surround(text, "__");
    }

    public String strikethrough(@NonNull String text) {
        return surround(text, "~");
    }

    public String spoiler(@NonNull String text) {
        return surround(text, "||");
    }

    public String inlineUrl(@NonNull String text, @NonNull String url) {
        StringBuilder inlineUrl = new StringBuilder();
        inlineUrl
            .append("[")
            .append(text)
            .append("]")
            .append("(")
            .append(url)
            .append(")");
        return inlineUrl.toString();
    }

    public String inlineUserMention(@NonNull String text, int userId) {
        StringBuilder inlineUserMention = new StringBuilder();
        inlineUserMention
            .append("[")
            .append(text)
            .append("]")
            .append("(tg://user?id=")
            .append(userId)
            .append(")");
        return inlineUserMention.toString();
    }

    public String inlineCodeBlock(@NonNull String text) {
        return surround(text, "`");
    }

    public String codeBlock(@NonNull String text) {
        return surround(surround(text, "\n"), "```");
    }

    private String surround(@NonNull String text, @NonNull String surround) {
        StringBuilder surroundedText = new StringBuilder();
        surroundedText
            .append(surround)
            .append(text)
            .append(surround);
        return surroundedText.toString();
    }
}
