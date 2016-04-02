package ru.simflex.ex.exceptions;

/**
 * Custom exception for Option class.
 */
public class PhoneNumberReadingException extends GenericException {
    public PhoneNumberReadingException(String message) {
        super(message);
    }

    public PhoneNumberReadingException(String message, Exception e) {
        super(message, e);
    }
}
