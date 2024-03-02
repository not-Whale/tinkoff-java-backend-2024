package edu.java.bot.exceptions;

public class EmptyTrackListException extends IllegalArgumentException {
    public EmptyTrackListException(String message) {
        super(message);
    }
}
