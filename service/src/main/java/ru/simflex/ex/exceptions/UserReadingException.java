package ru.simflex.ex.exceptions;

/**
 * Custom exception for User class.
 */
public class UserReadingException extends GenericException {
    public UserReadingException(String message) {
        super(message);
    }

    public UserReadingException(String message, Exception e) {
        super(message, e);
    }
}
