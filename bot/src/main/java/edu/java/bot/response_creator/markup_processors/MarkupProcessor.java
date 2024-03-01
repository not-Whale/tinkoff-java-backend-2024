package edu.java.bot.response_creators.markup_processors;

import com.pengrad.telegrambot.model.request.ParseMode;

public interface MarkupProcessor {
    ParseMode parseMode();

    String bold(String text);
}
