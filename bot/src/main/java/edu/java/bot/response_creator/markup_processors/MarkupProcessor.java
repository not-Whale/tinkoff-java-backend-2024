package edu.java.bot.response_creator.markup_processors;

import com.pengrad.telegrambot.model.request.ParseMode;

public interface MarkupProcessor {
    ParseMode parseMode();

    String bold(String text);

    String italic(String text);

    String inlineUrl(String text, String url);

    String inlineUserMention(String text, int userId);

    String inlineCodeBlock(String text);

    String codeBlock(String text);

    String escape(String text);
}
