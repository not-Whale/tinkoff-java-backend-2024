package edu.java.bot.exceptions;

public class NoSuchUserException extends IllegalArgumentException {
    public NoSuchUserException(String message) {
        super(message);
    }
}
