package ru.simflex.ex.exceptions;

/**
 * Custom exception for Option class.
 */
public class OptionDeletingException extends GenericException {
    public OptionDeletingException(String message) {
        super(message);
    }

    public OptionDeletingException(String message, Exception e) {
        super(message, e);
    }
}
