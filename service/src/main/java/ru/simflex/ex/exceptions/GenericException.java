package ru.simflex.ex.exceptions;

/**
 * Generic custom exception.
 */
public class GenericException extends RuntimeException {

    public GenericException(String message) {
        super(message);
    }

    public GenericException(String message, Exception e) {
        super(message, e);
    }
}
