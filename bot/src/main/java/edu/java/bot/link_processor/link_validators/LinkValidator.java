package edu.java.bot.link_processor.link_parsers;

public interface LinkParser {
    boolean validate();

    void parse();
}
