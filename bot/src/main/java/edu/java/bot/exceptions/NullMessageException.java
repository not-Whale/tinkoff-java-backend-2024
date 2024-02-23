package edu.java.bot.exceptions;

public class NullMessageException extends IllegalArgumentException {
    public NullMessageException(String message) {
        super(message);
    }
}
