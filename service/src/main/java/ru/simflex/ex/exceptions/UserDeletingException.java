package ru.simflex.ex.exceptions;

/**
 * Custom exception for User class.
 */
public class UserDeletingException extends GenericException {
    public UserDeletingException(String message) {
        super(message);
    }

    public UserDeletingException(String message, Exception e) {
        super(message, e);
    }
}
