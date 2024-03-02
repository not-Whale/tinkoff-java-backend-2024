package edu.java.bot.link_processor.link_validators;

import java.util.List;

public interface LinkValidator {
    List<String> patterns();

    default boolean validate(String link) {
        return patterns().stream().anyMatch(link::matches);
    }
}
