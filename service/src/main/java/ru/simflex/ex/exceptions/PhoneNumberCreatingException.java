package ru.simflex.ex.exceptions;

/**
 * Custom exception for Option class.
 */
public class PhoneNumberCreatingException extends GenericException {
    public PhoneNumberCreatingException(String message) {
        super(message);
    }

    public PhoneNumberCreatingException(String message, Exception e) {
        super(message, e);
    }
}
