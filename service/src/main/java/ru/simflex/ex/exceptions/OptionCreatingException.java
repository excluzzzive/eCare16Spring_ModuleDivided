package ru.simflex.ex.exceptions;

/**
 * Custom exception for Option class.
 */
public class OptionCreatingException extends GenericException {
    public OptionCreatingException(String message) {
        super(message);
    }

    public OptionCreatingException(String message, Exception e) {
        super(message, e);
    }
}
