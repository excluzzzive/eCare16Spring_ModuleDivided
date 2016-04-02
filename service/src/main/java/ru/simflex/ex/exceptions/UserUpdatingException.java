package ru.simflex.ex.exceptions;

/**
 * Custom exception for User class.
 */
public class UserUpdatingException extends GenericException {
    public UserUpdatingException(String message) {
        super(message);
    }

    public UserUpdatingException(String message, Exception e) {
        super(message, e);
    }
}
