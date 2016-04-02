package ru.simflex.ex.exceptions;

/**
 * Custom exception for Option class.
 */
public class OptionUpdatingException extends GenericException {
    public OptionUpdatingException(String message) {
        super(message);
    }

    public OptionUpdatingException(String message, Exception e) {
        super(message, e);
    }
}
