package edu.java.bot.exceptions;

public class CommandDoesNotSupportsException extends IllegalArgumentException {
    public CommandDoesNotSupportsException(String message) {
        super(message);
    }
}
