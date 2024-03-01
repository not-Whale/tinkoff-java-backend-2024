package edu.java.bot.response_processors;

import edu.java.bot.response_processors.markup_processors.MarkupProcessor;

public class ResponseMessageProcessor {
    private final MarkupProcessor markupProcessor;

    public ResponseMessageProcessor(MarkupProcessor markupProcessor) {
        this.markupProcessor = markupProcessor;
    }
}
