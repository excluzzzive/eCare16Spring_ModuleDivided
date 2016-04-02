package ru.simflex.ex.exceptions;

/**
 * Custom exception for Option class.
 */
public class OptionReadingException extends GenericException {
    public OptionReadingException(String message) {
        super(message);
    }

    public OptionReadingException(String message, Exception e) {
        super(message, e);
    }
}
