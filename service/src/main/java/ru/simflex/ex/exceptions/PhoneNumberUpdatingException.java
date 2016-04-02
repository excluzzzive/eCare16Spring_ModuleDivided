package ru.simflex.ex.exceptions;

/**
 * Custom exception for Option class.
 */
public class PhoneNumberUpdatingException extends GenericException {
    public PhoneNumberUpdatingException(String message) {
        super(message);
    }

    public PhoneNumberUpdatingException(String message, Exception e) {
        super(message, e);
    }
}
