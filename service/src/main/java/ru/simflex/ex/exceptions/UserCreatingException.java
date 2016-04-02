package ru.simflex.ex.exceptions;

/**
 * Custom exception for User class.
 */
public class UserCreatingException extends GenericException {
    public UserCreatingException(String message) {
        super(message);
    }

    public UserCreatingException(String message, Exception e) {
        super(message, e);
    }
}
