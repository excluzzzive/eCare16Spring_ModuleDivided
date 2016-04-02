package ru.simflex.ex.exceptions;

/**
 * Custom exception for Option class.
 */
public class PhoneNumberDeletingException extends GenericException {
    public PhoneNumberDeletingException(String message) {
        super(message);
    }

    public PhoneNumberDeletingException(String message, Exception e) {
        super(message, e);
    }
}
