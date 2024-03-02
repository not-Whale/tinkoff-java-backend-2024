package edu.java.bot.link_processor;

import edu.java.bot.link_processor.link_validators.LinkValidator;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class LinkProcessor {
    private final List<LinkValidator> linkValidators;

    public LinkProcessor(@NonNull List<LinkValidator> linkValidators) {
        this.linkValidators = linkValidators;
    }

    public List<String> getValidatedLinks(@NonNull List<String> links) {
        List<String> validatedLinks = new ArrayList<>();
        for (String link : links) {
            if (linkValidators.stream().anyMatch(linkValidator -> linkValidator.validate(link))) {
                validatedLinks.add(link);
            }
        }
        return validatedLinks;
    }

    public List<String> getUnvalidatedLinks(@NonNull List<String> links) {
        List<String> unvalidatedLinks = new ArrayList<>();
        for (String link : links) {
            if (linkValidators.stream().noneMatch(linkValidator -> linkValidator.validate(link))) {
                unvalidatedLinks.add(link);
            }
        }
        return unvalidatedLinks;
    }
}
