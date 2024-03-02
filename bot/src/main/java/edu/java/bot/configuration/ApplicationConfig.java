package edu.java.bot.configuration;

import edu.java.bot.response_creator.markup_processors.MarkdownProcessor;
import edu.java.bot.response_creator.markup_processors.MarkupProcessor;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(@NotEmpty String telegramToken, @NotEmpty String languageCode) {
    @Bean
    public MarkupProcessor markupProcessor() {
        return new MarkdownProcessor();
    }
}
