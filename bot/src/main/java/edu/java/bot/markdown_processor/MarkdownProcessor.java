package edu.java.bot.markdown_processor;

public class MarkdownProcessor {
    private MarkdownProcessor() {}

    public static String bold(String text) {
        return surround(text, "*");
    }

    public static String italic(String text) {
        return surround(text, "_");
    }

    public static String underline(String text) {
        return surround(text, "__");
    }

    public static String strikethrough(String text) {
        return surround(text, "~");
    }

    public static String spoiler(String text) {
        return surround(text, "||");
    }

    public static String inlineUrl(String text, String url) {
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

    public static String inlineUserMention(String text, int userId) {
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

    public static String inlineCodeBlock(String text) {
        return surround(text, "`");
    }

    public static String codeBlock(String text) {
        return surround(surround(text, "\n"), "```");
    }

    private static String surround(String text, String surround) {
        StringBuilder surroundedText = new StringBuilder();
        surroundedText
            .append(surround)
            .append(text)
            .append(surround);
        return surroundedText.toString();
    }
}
