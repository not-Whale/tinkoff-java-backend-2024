package edu.java.bot.markdown_processor;

import lombok.NonNull;

public class MarkdownProcessor {
    private MarkdownProcessor() {}

    public static String bold(@NonNull String text) {
        return surround(text, "*");
    }

    public static String italic(@NonNull String text) {
        return surround(text, "_");
    }

    public static String underline(@NonNull String text) {
        return surround(text, "__");
    }

    public static String strikethrough(@NonNull String text) {
        return surround(text, "~");
    }

    public static String spoiler(@NonNull String text) {
        return surround(text, "||");
    }

    public static String inlineUrl(@NonNull String text, @NonNull String url) {
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

    public static String inlineUserMention(@NonNull String text, int userId) {
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

    public static String inlineCodeBlock(@NonNull String text) {
        return surround(text, "`");
    }

    public static String codeBlock(@NonNull String text) {
        return surround(surround(text, "\n"), "```");
    }

    private static String surround(@NonNull String text, @NonNull String surround) {
        StringBuilder surroundedText = new StringBuilder();
        surroundedText
            .append(surround)
            .append(text)
            .append(surround);
        return surroundedText.toString();
    }
}
